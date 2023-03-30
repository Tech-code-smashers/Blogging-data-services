package com.blog.user.serviceImpl;

import com.blog.user.entity.Users;
import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import com.blog.user.model.UserDto;
import com.blog.user.repository.UserDao;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.UserService;
import com.blog.user.utils.CommonUtils;
import com.blog.user.utils.DataTransformation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.LIST_NULL;
import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.NOT_FOUND;
import static com.blog.user.utils.CommonUtils.RESPONSE_MESSAGE.DELETE_SUCCESS;
import static com.blog.user.utils.CommonUtils.RESPONSE_MESSAGE.FETCH_ALL_DETAILS;
import static com.blog.user.utils.CommonUtils.StatusCode.SUCCESS;

@Service
public class UserServiceImpl  implements UserService {
        Logger LOGGER =  LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao repo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DataTransformation dataTransformation;

    private UserDto etm(Users user){
      return  mapper.map(user,UserDto.class);
    }

    private Users mte(UserDto userDto){
        return  mapper.map(userDto,Users.class);
    }

    @Override
    public CommonControllerResponse<UserDto> insertOrUpdate(UserDto userDto) {
        CommonControllerResponse<UserDto> response = new CommonControllerResponse<>();
        Users user=null;
        try {
            if (userDto != null && userDto.getId() != 0 && userDto.getId() != null) {
                user = dataTransformation.userDataTransform(userDto);
                 response.setMessage(CommonUtils.RESPONSE_MESSAGE.UPDATE_SUCCESS);
                 response.setData(etm(repo.save(user)));
                 LOGGER.info("Update data payload :{}",user);
            }else{

                response.setMessage(CommonUtils.RESPONSE_MESSAGE.SAVE_SUCCESS);
                response.setData(etm(repo.save( repo.save(mte(userDto)))));
                LOGGER.info("save data payload :{}",userDto);

            }
        }catch (Exception ex){
            response.setMessage(ex.getCause().getMessage());
            LOGGER.error("Error msg {}", ex.getCause().getMessage());
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
           // PageRequest page = PageRequest.of(pageNo, pageSize, sorting);
            List<Users> list = repo.findAll();
            if (list.size() > 0) {
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
    public CommonControllerResponse<UserDto> findByUserName(String userName) {
        CommonControllerResponse<UserDto> response = new CommonControllerResponse<>();
        try {
            UserDto userDto = etm(repo.findByUserName(userName));
            if (userDto != null) {
                response.setMessage(SUCCESS);
                response.setData(userDto);
            }else{
                response.setMessage(NOT_FOUND);
            }
        }catch (Exception ex){
            response.setMessage(ex.getCause().getMessage());
        }
        return response;
    }

}
