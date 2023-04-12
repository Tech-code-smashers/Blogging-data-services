package com.blog.user.serviceImpl;


import com.blog.user.entity.BlogCategory;
import com.blog.user.globalExceptions.exception.IllegalArgumentException;
import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import com.blog.user.model.CategoryDto;
import com.blog.user.repository.CategoryDao;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.CategoryService;
import com.blog.user.utils.CommonUtils;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.*;
import static com.blog.user.utils.CommonUtils.RESPONSE_MESSAGE.*;
import static com.blog.user.utils.CommonUtils.StatusCode.SUCCESS;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CategoryDao repo;
    public BlogCategory mte(CategoryDto categoryDto) {
        return mapper.map(categoryDto, BlogCategory.class);
    }
    public CategoryDto etm(BlogCategory blogCategory) {
        return mapper.map(blogCategory, CategoryDto.class);
    }


    @Transactional(rollbackFor = HibernateException.class)
    @Override
    public CommonControllerResponse<CategoryDto> insertOrUpdate(CategoryDto categoryDto) {
        CommonControllerResponse<CategoryDto> response = new CommonControllerResponse<>();
        BlogCategory blogCategory = null;
        try {
            if (categoryDto != null) {
                if (categoryDto.getId() > 0 && categoryDto.getId() != 0) {
                        blogCategory = repo.findById(categoryDto.getId()).get();
                    blogCategory = mte(categoryDto);
                    response.setMessage(CommonUtils.RESPONSE_MESSAGE.UPDATE_SUCCESS);
                    response.setData(etm(repo.save(blogCategory)));
                }else {
                    response.setMessage(CommonUtils.RESPONSE_MESSAGE.SAVE_SUCCESS);
                    response.setData(etm(repo.save(mte(categoryDto))));
                    LOGGER.info("save data payload :{}", categoryDto);
                }
            }else {
                response.setMessage("User Dto cannot Be null");
            }
        } catch (Exception ex) {
            if (ex.getClass() == DataIntegrityViolationException.class) {
                throw new DataIntegrityViolationException(ex.getCause().getCause().getMessage());

            } else {
                response.setMessage(ex.getCause().getMessage());
                LOGGER.error("Error msg {}", ex.getCause().getMessage());
            }
        }
        return response;
    }

    @Override
    public CommonControllerResponse<String> delete(List<Integer> ids) {
        CommonControllerResponse<String> response = new CommonControllerResponse<>();
        if(ids.size()>0){
                ids.stream().forEach(id ->{
                    repo.findById(id).orElseThrow(()->new ResourceNotFoundException(id +" "+NOT_FOUND));
                    repo.deleteById(id);
                });
                response.setMessage(DELETE_SUCCESS);
        } else{
            response.setMessage(String.valueOf(HttpStatus.BAD_REQUEST));
        }
        return response;
    }

    @Override
    public CommonControllerResponse<List<CategoryDto>> findAllWithPaginationAndSorting(int page, int size, String sortBy, String sortDir) {
        CommonControllerResponse<List<CategoryDto>> response = new CommonControllerResponse<>();
        Sort sortByDir = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sortByDir);
        Page<BlogCategory> dataWithPagination= repo.findAll(pageable);
        List<BlogCategory> listData= dataWithPagination.getContent();
        if(listData.size()>0){
           List<CategoryDto> categoryDtoList = listData.stream().map(data->etm(data)).collect(Collectors.toList());
           response.setMessage(FETCH_ALL_DETAILS);
           response.setData(categoryDtoList);
        }
        else{
            response.setMessage(LIST_NULL);
        }
        return response;
    }

    @Override
    public CommonControllerResponse<CategoryDto> findByCategoryTitle(String categoryTitle) {
        CommonControllerResponse<CategoryDto> response = new CommonControllerResponse<>();
        try {
            CategoryDto categoryDto = etm(repo.findByCategoryTitle(categoryTitle));
            response.setMessage(FETCH_BY_CATEGORY_TITLE);
            response.setData(categoryDto);
        }catch (Exception ex){
         Exception exception = new IllegalArgumentException();
         response.setMessage(exception.getMessage());
        }
         return response;
    }

    @Override
    public CommonControllerResponse<CategoryDto> findById(Integer id) {
        CommonControllerResponse<CategoryDto> response = new CommonControllerResponse<>();
        if(id>0){
            response.setMessage(SUCCESS);
            response.setData(etm(repo.findById(id).orElseThrow(()->new ResourceNotFoundException(id +" "+NOT_FOUND))));
        }
        else{
            response.setMessage(ILLEGAL_ARGUMENT);
        }
        return response;
    }
}
