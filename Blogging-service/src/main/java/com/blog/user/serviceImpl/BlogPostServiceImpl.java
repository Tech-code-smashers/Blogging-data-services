package com.blog.user.serviceImpl;

import com.blog.user.entity.BlogCategory;
import com.blog.user.entity.BlogPost;
import com.blog.user.entity.Users;
import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import com.blog.user.model.BlogPostDto;
import com.blog.user.repository.BlogPostDao;
import com.blog.user.repository.CategoryDao;
import com.blog.user.repository.UserDao;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.BlogPostService;
import com.blog.user.utils.CommonUtils;
import com.blog.user.utils.DataTransformation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.*;
import static com.blog.user.utils.CommonUtils.RESPONSE_MESSAGE.*;

@Service
public class BlogPostServiceImpl implements BlogPostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPostServiceImpl.class);

    @Autowired
    private DataTransformation transformation;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private BlogPostDao repo;
    @Autowired
    private UserDao userRepo;
    @Autowired
    private CategoryDao categoryDao;

    public BlogPost mte(BlogPostDto blogPostDto) { return mapper.map(blogPostDto, BlogPost.class);}
    public BlogPostDto etm(BlogPost blogPost) {return mapper.map(blogPost, BlogPostDto.class);}

    @Override
    public CommonControllerResponse<BlogPostDto> insertOrUpdate(BlogPostDto postDto
            ,Integer categoryId, Integer userId) {
        CommonControllerResponse<BlogPostDto> response = new CommonControllerResponse<>();
        BlogPost blogPost;
        try {
             BlogCategory category = categoryDao.findById(categoryId).orElseThrow(()->new ResourceNotFoundException());
             Users user =userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException());
            if (postDto != null) {
                if (postDto.getId() > 0 && postDto.getId() != 0) {
                    blogPost =  repo.findById(postDto.getId()).orElseThrow(()->new ResourceNotFoundException());
                    blogPost= transformation.BlogDataTransform(postDto,blogPost);
                    blogPost.setUser(user);
                    blogPost.setCategory(category);
                    response.setMessage(CommonUtils.RESPONSE_MESSAGE.UPDATE_SUCCESS);
                    response.setData(etm(repo.save(blogPost)));
                }else {
                    response.setMessage(CommonUtils.RESPONSE_MESSAGE.SAVE_SUCCESS);
                    BlogPost post = mte(postDto);
                    post.setImageName("Default.png");
                    post.setCategory(category);
                    post.setUser(user);
                    response.setData(etm(repo.save(post)));
                    LOGGER.info("save data payload :{}", postDto);
                }
            }else {
                response.setMessage("post data cannot Be null");
            }
        } catch (Exception ex) {
            if (ex.getClass() == DataIntegrityViolationException.class)
                throw new DataIntegrityViolationException(ex.getCause().getCause().getMessage());
            else
                response.setMessage(ex.getCause().getMessage());
                LOGGER.error("Error msg {}", ex.getCause().getMessage());
        }
        return response;    }

    @Override
    public CommonControllerResponse<String> delete(List<Integer> ids) {
        CommonControllerResponse<String> response = new CommonControllerResponse<>();
        if(ids.size()>0)
            ids.stream().forEach(id->{
                repo.findById(id).orElseThrow(()->new ResourceNotFoundException());
                repo.deleteById(id);
                response.setMessage(DELETE_SUCCESS);
            });
        else
            response.setMessage(EMPTY_LIST);
        return response;
    }

    @Override
    public CommonControllerResponse<List<BlogPostDto>> findAllWithPaginationAndSorting(int page, int size, String sortBy, String sortDir) {
        CommonControllerResponse<List<BlogPostDto>> response = new CommonControllerResponse<>();
        Sort sortByDir = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sortByDir);
        Page<BlogPost> dataWithPagination= repo.findAll(pageable);
        List<BlogPost> listData= dataWithPagination.getContent();
        if(listData.size()>0) {
            List<BlogPostDto> postDtoList = listData.stream().map(data -> etm(data)).collect(Collectors.toList());
            response.setMessage(FETCH_ALL_DETAILS);
            response.setData(postDtoList);
        }else {
            response.setMessage(LIST_NULL);
        }
        return response;
    }

    @Override
    public CommonControllerResponse<List<BlogPostDto>> findByCategoryId(Integer categoryId) {
        CommonControllerResponse<List<BlogPostDto>> response = new CommonControllerResponse<>();
            BlogCategory blogCategory =categoryDao.findById(categoryId).orElseThrow(()->new ResourceNotFoundException(categoryId + " "+ NOT_FOUND));
            List<BlogPost> blogPostList = repo.findByCategoryId(blogCategory.getId());
            response.setMessage(SUCCESS);
            response.setData(blogPostList.stream().map(obj->etm(obj)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public CommonControllerResponse<BlogPostDto> findById(Integer id) {
        CommonControllerResponse<BlogPostDto> response = new CommonControllerResponse<>();
        if (id > 0) {
           BlogPost blogPost = (repo.findById(id).orElseThrow(() -> new ResourceNotFoundException()));
            response.setMessage(SUCCESS);
            response.setData(etm(blogPost));
        }else {
            response.setMessage(EMPTY_LIST);
        }
        return response;
    }
}
