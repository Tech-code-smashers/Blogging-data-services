package com.blog.user.serviceImpl;

import com.blog.email.payload.EmailDetails;
import com.blog.email.service.EmailService;
import com.blog.user.entity.Users;
import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import com.blog.user.model.UserDto;
import com.blog.user.repository.UserDao;
import com.blog.user.request.UsersExcelClass;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.responses.responses.CommonExcelResponse;
import com.blog.user.service.UserService;
import com.blog.user.utils.BlogExcelUtils;
import com.blog.user.utils.BlogUtils;
import com.blog.user.utils.CommonUtils;
import com.blog.user.utils.DataTransformation;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.*;
import static com.blog.user.utils.CommonUtils.RESPONSE_MESSAGE.DELETE_SUCCESS;
import static com.blog.user.utils.CommonUtils.RESPONSE_MESSAGE.FETCH_ALL_DETAILS;
import static com.blog.user.utils.CommonUtils.StatusCode.SUCCESS;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl  implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao repo;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DataTransformation dataTransformation;

    @Autowired
    private EmailService emailService;

    private UserDto etm(Users user){
      return  mapper.map(user,UserDto.class);
    }

    private Users mte(UserDto userDto){
        return  mapper.map(userDto,Users.class);
    }


    @Transactional(rollbackFor = HibernateException.class)
    @Override
    public CommonControllerResponse<UserDto> insertOrUpdate(UserDto userDto) {
        CommonControllerResponse<UserDto> response = new CommonControllerResponse<>();
        Users user = null;
        user = dataTransformation.userDataTransform(userDto);
        try {
            if (userDto != null && userDto.getId() != 0 && userDto.getId() != null) {
                user = repo.findById(userDto.getId()).get();
                 response.setMessage(CommonUtils.RESPONSE_MESSAGE.UPDATE_SUCCESS);
                 response.setData(etm(repo.save(user)));
                 LOGGER.info("Update data payload :{}",user);
            }else{
                response.setMessage(CommonUtils.RESPONSE_MESSAGE.SAVE_SUCCESS);
                response.setData(etm(repo.save(user)));
                EmailDetails emailDetails =dataTransformation.emailBody(user);
                emailService.sendSimpleMail(emailDetails);
                LOGGER.info("save data payload :{}",userDto);

            }
        } catch (Exception ex){
            if(ex.getClass()==DataIntegrityViolationException.class){
                throw new DataIntegrityViolationException(ex.getCause().getCause().getMessage());
            }else{
                response.setMessage(ex.getCause().getMessage());
                LOGGER.error("Error msg {}", ex.getCause().getMessage());
            }
        }
        return response;
    }

    @Override
    public CommonControllerResponse<String> Delete(List<Integer> ids) {
        CommonControllerResponse<String> response = new CommonControllerResponse<>();
        if(ids.size()>0){
            ids.forEach(e->{
                repo.findById(e).orElseThrow(() -> new ResourceNotFoundException(e +" "+NOT_FOUND));
                try {
                    repo.deleteById(e);
                }catch (Exception ex){
                    throw new ResourceNotFoundException(NOT_FOUND);
                }
            });
            response.setMessage(DELETE_SUCCESS);
            response.setStatus(response.getStatus());
        }else{
            response.setMessage(String.valueOf(HttpStatus.BAD_REQUEST));
        }
        return response;
    }

    @Override
    public CommonControllerResponse<List<UserDto>> findAllWithPaginationAndSorting(int pageNo, Integer pageSize, String sortBy, String sortDir) {
        CommonControllerResponse<List<UserDto>> response= new CommonControllerResponse<>();
        try {
            Sort sorting = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable page = PageRequest.of(pageNo, pageSize, sorting);
            Page<Users> list = repo.findAll(page);
            List<Users> listData= list.getContent();
            if (listData.size() > 0) {
                List<UserDto> userList = list.stream().map((mapObj -> etm(mapObj))).collect(Collectors.toList());
                response.setMessage(FETCH_ALL_DETAILS);
                response.setData(userList);
            } else {
                response.setMessage(LIST_NULL);
            }
        }catch (Exception ex){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
            response.setMessage(ex.getCause().getMessage());
        }
        return response;
    }

    @Override
    public CommonControllerResponse<UserDto> findByUserName(String userName)  {
        CommonControllerResponse<UserDto> response = new CommonControllerResponse<>();
        try{
            UserDto userDto = etm(repo.findByUserName(userName));
                response.setMessage(SUCCESS);
                response.setData(userDto);
        }
        catch (RuntimeException ex){
          Exception e = new ResourceNotFoundException(userName+" "+NOT_FOUND);
          response.setMessage(e.getLocalizedMessage());
        }
        return response;
    }

    @Override
    public CommonControllerResponse<UserDto> findById(Integer id) {
        CommonControllerResponse<UserDto> response = new CommonControllerResponse<>();
        if(id!=0 && id>0){
            Users users = repo.findById(id).orElseThrow(()-> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
                if(users!=null){
                    response.setMessage(SUCCESS);
                    response.setData(etm(users));
                }
        }
        return response;
    }

    @Override
    public ByteArrayInputStream getUserExcelTemplate(String excelType) {
       List<String> columnList = BlogUtils.getColumns(UserDto.class);
        return BlogExcelUtils.getExcelTemplate(SHEET_NAME,columnList,excelType);
    }

    @Override
    public List<CommonExcelResponse> uploadExcelData(InputStream fileData, String fileName) throws IOException {
        List<Map<String,Object>>  excelData =  BlogUtils.excelDataMap(SHEET_NAME,fileData,fileName);
          List<CommonExcelResponse> responses =  excelData.parallelStream().map(mapObj -> {
                CommonExcelResponse uploadResponse = new CommonExcelResponse(CommonUtils.RESPONSE_MESSAGE.SUCCESS,null,null);
                try {
                    Integer id = (Integer) mapObj.get(BlogUtils.getColumns(UserDto.class).get(0) == null ? 0 : Integer.parseInt(BlogUtils.getColumns(UserDto.class).get(0)));
                    String name = (String) mapObj.get(BlogUtils.getColumns(UserDto.class).get(1));
                    String lastName = (String) mapObj.get(BlogUtils.getColumns(UserDto.class).get(2));
                    String userName = (String) mapObj.get(BlogUtils.getColumns(UserDto.class).get(3));
                    String email = (String) mapObj.get(BlogUtils.getColumns(UserDto.class).get(4));
                    String password = (String) mapObj.get(BlogUtils.getColumns(UserDto.class).get(5));
                    String about = (String) mapObj.get(BlogUtils.getColumns(UserDto.class).get(6));

                    UserDto dto = new UserDto();
                    dto.setId(id);
                    dto.setName(name);
                    dto.setLastName(lastName);
                    dto.setUserName(userName);
                    dto.setPassword(password);
                    dto.setEmail(email);
                    dto.setAbout(about);
                    bulkSaveOrUpdate(dto);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return uploadResponse;
            }).collect(Collectors.toList());
            return responses;
    }



    @Override
    public ResponseEntity<byte[]> downloadCsvFile() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Id,Name,Last Name,user name, Email, password,about,Date\n");
        List<Users> data = repo.findAll();
        data.forEach(csvData->{
            String column1Data = csvData.getId().toString();
            String column2data = csvData.getName();
            String column3data = csvData.getLastName();
            String column4data = csvData.getUserName();
            String column5data = csvData.getEmail();
            String column6data = csvData.getPassword();
            String column7data = csvData.getAbout();
            String column8data = csvData.getDate().toString();
            csvBuilder.append(column1Data).append(",");
            csvBuilder.append(column2data).append(",");
            csvBuilder.append(column3data).append(",");
            csvBuilder.append(column4data).append(",");
            csvBuilder.append(column5data).append(",");
            csvBuilder.append(column6data).append(",");
            csvBuilder.append(column7data).append(",");
            csvBuilder.append(column8data).append("\n");

        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "Users.csv");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(csvBuilder.toString().getBytes(),headers,HttpStatus.OK);
    }

    @Override
    public Workbook uploadUserExcel(InputStream fileData, String fileName) throws Exception {
        BlogExcelUtils blogExcelUtils=context.getBean(BlogExcelUtils.class);
        Workbook excelDataList = blogExcelUtils.submitExcelToProcess(fileData, SHEET_NAME, fileName,
                UsersExcelClass.class);
        return excelDataList;
    }


    public void bulkSaveOrUpdate(UserDto userDto){
       insertOrUpdate(userDto);
    }

}
