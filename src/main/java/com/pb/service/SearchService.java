package com.pb.service;

import com.pb.config.GlobalConfiguration;
import com.pb.dto.ParameterDTO;
import com.pb.dto.search.SearchDTO;
import com.pb.dto.search.SearchPaginationResDTO;
import com.pb.model.UserEntity;
import com.pb.repository.*;
import com.pb.security.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SearchService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    ProfileCompanyDetailsRepository profileCompanyDetailsRepository;
    @Autowired
    ProfileMemberInfoRepository profileMemberInfoRepository;
    @Autowired
    ProfileCategoryAndProductRepository profileCategoryAndProductRepository;
    @Autowired
    ProfileArtWorkRepository profileArtWorkRepository;
    @Autowired
    ProfileQARepository profileQARepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    TimezoneRepository timezoneRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    AWSS3Service awss3Service;
    @Autowired
    private GlobalConfiguration globalConfiguration;
    @Autowired
    private PrivateBrandRepository privateBrandRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenUtils jwtTokenUtil;

    public SearchPaginationResDTO searchOnDb(String header, ParameterDTO parameterDTO) { // product name, product desc, username.
        String userEmail = jwtTokenUtil.getUsernameFromToken(header);
        Optional<UserEntity> byEmail = userRepository.findByEmail(userEmail);
        if(byEmail.isPresent()){
            parameterDTO.setUserType(byEmail.get().getUserType().equals("RETAILER")?"SUPPLIER":"RETAILER");
        }
        int pagesize = 12;
        String userType = parameterDTO.getUserType();
        String key = parameterDTO.getKey();
        Long departmentId = parameterDTO.getDepartmentId();
        Long categoryId = parameterDTO.getCategoryId();
        String selectSQL = " select distinct u.id as userId,u.profile_url AS profileUrl, u.user_name AS memberName,u.company_name as companyName, pcd.company_logo_url as companyLogo, ";
        selectSQL += "IF(u.id in (select network_id from user_network where user_id = "+parameterDTO.getUserId()+"), TRUE, FALSE) as inNetwork from user u ";
        String selectCountSQL = "select count(distinct u.id) from user u ";
        String join = " left join product p on (u.id = p.user_id) left join profile pr on (u.id = pr.user_id) left join profile_company_details pcd on (pr.company_detail_id = pcd.id) ";
        String whereSQL = " where p.deleted_date is null and u.user_type = \""+ userType +"\"";
        if(departmentId != null){
            whereSQL += " and p.department_id = "+departmentId;
        }
        if(categoryId!= null){
            whereSQL += " and p.category_id = "+categoryId;
        }
        if(key != null && key != ""){
            whereSQL += " and ( u.company_name LIKE '%"+key+"%' or p.name LIKE '%"+key+"%' or p.description LIKE '%"+key+"%') ";
        } if(parameterDTO.getForNetwork()){
            whereSQL += " and u.id in (select network_id from user_network where user_id = "+parameterDTO.getUserId()+")";
        }
        //whereSQL += " and pr.artwork_id is not null and pr.category_product_id is not null and pr.company_detail_id is not null and pr.member_info_id is not null and pr.qa_id is not null";
        int offset = (parameterDTO.getPageNumber() - 1) * pagesize;
        String sql = selectSQL + join + whereSQL +" limit 12 offset "+offset;
        String countSql = selectCountSQL + join + whereSQL ;
        int totalItems = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPages = (int) Math.ceil((double) totalItems / pagesize);
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        List<SearchDTO> employees = jdbcTemplate.query(sql, (rs, rowNum) -> {
            SearchDTO employee = new SearchDTO();
            employee.setUserId(rs.getLong("userId"));
            employee.setCompanyName(rs.getString("companyName"));
            employee.setInNetwork(rs.getBoolean("inNetwork"));
            employee.setMemberName(rs.getString("memberName"));
            String companyLogo = rs.getString("companyLogo");
            String filePath = amazonS3.getS3BaseUrl() + amazonS3.getUploadFolderName()+amazonS3.getUserProfile() + amazonS3.getCompanyLogo() + companyLogo;
            employee.setCompanyLogo(filePath);
            String profileUrl = rs.getString("profileUrl");
            filePath = amazonS3.getS3BaseUrl() + amazonS3.getUploadFolderName()+ amazonS3.getProfilePhoto() + profileUrl;
            employee.setProfilePath(filePath);
            return employee;
        });
        return new SearchPaginationResDTO(employees,totalPages);
    }
}
