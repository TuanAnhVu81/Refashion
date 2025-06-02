package exe201.Refashion.configuration;

import exe201.Refashion.entity.Categories;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.ProductCondition;
import exe201.Refashion.repository.CategoryRepository;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.RoleRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import exe201.Refashion.entity.Role;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        CategoryRepository categoriesRepository,
                                        ProductRepository productsRepository) {
        return args -> {
            // Tạo Roles
            if (roleRepository.count() == 0 ) {
                // Lấy Role ADMIN từ database
                List<Role> roles = List.of(
                        new Role("1", "ADMIN","admin",true ),
                        new Role("2", "USER","user",true )
                );
                roleRepository.saveAll(roles);
                log.warn("All roles have been created");
            }

            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                // Tạo Admin User
                Role adminRole = roleRepository.findByRoleName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Role ADMIN not found!"));

                Users user = Users.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .emailVerified(true)
                        .password(passwordEncoder.encode("admin123"))
                        .role((adminRole))  // Gán Role entity
                        .build();

                userRepository.save(user);
                log.warn("Admin has been created with default password admin123, please change it");
            }

//            // Tạo Sample Users (Buyers và Sellers)
//            if (userRepository.count() <= 1) {
//                Role buyerRole = roleRepository.findByRoleName("BUYER")
//                        .orElseThrow(() -> new RuntimeException("Role BUYER not found!"));
//                Role sellerRole = roleRepository.findByRoleName("SELLER")
//                        .orElseThrow(() -> new RuntimeException("Role SELLER not found!"));
//
//                // Tạo Buyers
//                List<Users> buyers = List.of(
//                        Users.builder()
//                                .username("buyer1")
//                                .email("buyer1@gmail.com")
//                                .password(passwordEncoder.encode("password123"))
//                                .role(buyerRole)
//                                .fullName("Nguyen Van An")
//                                .phoneNumber("0912345678")
//                                .address("District 1, Ho Chi Minh City")
//                                .emailVerified(true)
//                                .active(true)
//                                .build(),
//                        Users.builder()
//                                .username("buyer2")
//                                .email("buyer2@gmail.com")
//                                .password(passwordEncoder.encode("password123"))
//                                .role(buyerRole)
//                                .fullName("Le Thi Bich")
//                                .phoneNumber("0976543210")
//                                .address("District 3, Ho Chi Minh City")
//                                .emailVerified(true)
//                                .active(true)
//                                .build()
//
//                );
//
//                // Tạo Sellers
//                List<Users> sellers = List.of(
//                        Users.builder()
//                                .username("seller1")
//                                .email("seller1@gmail.com")
//                                .password(passwordEncoder.encode("password123"))
//                                .role(sellerRole)
//                                .fullName("Pham Thi Dao")
//                                .phoneNumber("0945678901")
//                                .address("District 7, Ho Chi Minh City")
//                                .emailVerified(true)
//                                .active(true)
//                                .build(),
//                        Users.builder()
//                                .username("seller2")
//                                .email("seller2@gmail.com")
//                                .password(passwordEncoder.encode("password123"))
//                                .role(sellerRole)
//                                .fullName("Tran Van Cuong")
//                                .phoneNumber("0934567890")
//                                .address("District 5, Ho Chi Minh City")
//                                .emailVerified(true)
//                                .active(true)
//                                .build()
//                );
//
//                userRepository.saveAll(buyers);
//                userRepository.saveAll(sellers);
//                log.warn("Sample buyers and sellers have been created");
//            }
//
//            // Tạo Categories
//            if (categoriesRepository.count() == 0) {
//                List<Categories> categories = List.of(
//                        Categories.builder()
//                                .id("CAT001")
//                                .name("Áo thun")
//                                .description("Các loại áo thun nam nữ")
//                                .build(),
//                        Categories.builder()
//                                .id("CAT002")
//                                .name("Quần jeans")
//                                .description("Quần jeans các loại")
//                                .build(),
//                        Categories.builder()
//                                .id("CAT003")
//                                .name("Áo sơ mi")
//                                .description("Áo sơ mi công sở và thường ngày")
//                                .build()
//                );
//
//                categoriesRepository.saveAll(categories);
//                log.warn("Categories have been created");
//            }
//
//            // Tạo Sample Products
//            if (productsRepository.count() == 0) {
//                // Lấy sellers và categories
//                List<Users> sellers = userRepository.findByRole_RoleName("SELLER");
//                List<Categories> categories = categoriesRepository.findAll();
//
//                if (!sellers.isEmpty() && !categories.isEmpty()) {
//                    List<Products> products = List.of(
//                            Products.builder()
//                                    .seller(sellers.get(0))
//                                    .title("Áo thun cotton nam")
//                                    .description("Áo thun cotton 100% màu trắng, size M, còn mới 95%")
//                                    .category(categories.get(0)) // Áo thun
//                                    .brand("Uniqlo")
//                                    .productCondition(ProductCondition.LIKE_NEW)
//                                    .size("M")
//                                    .color("Trắng")
//                                    .price(new BigDecimal("150000"))
//                                    .isActive(true)
//                                    .isFeatured(false)
//                                    .isSold(false)
//                                    .build(),
//                            Products.builder()
//                                    .seller(sellers.get(0))
//                                    .title("Quần jeans skinny nữ")
//                                    .description("Quần jeans skinny màu xanh đen, size 27, ít sử dụng")
//                                    .category(categories.get(1)) // Quần jeans
//                                    .brand("Zara")
//                                    .productCondition(ProductCondition.GOOD)
//                                    .size("27")
//                                    .color("Xanh đen")
//                                    .price(new BigDecimal("300000"))
//                                    .isActive(true)
//                                    .isFeatured(true)
//                                    .featuredUntil(LocalDateTime.now().plusDays(30))
//                                    .isSold(false)
//                                    .build(),
//                            Products.builder()
//                                    .seller(sellers.size() > 1 ? sellers.get(1) : sellers.get(0))
//                                    .title("Áo sơ mi công sở")
//                                    .description("Áo sơ mi trắng công sở, size L, đã qua sử dụng nhưng còn tốt")
//                                    .category(categories.get(2)) // Áo sơ mi
//                                    .brand("H&M")
//                                    .productCondition(ProductCondition.FAIR)
//                                    .size("L")
//                                    .color("Trắng")
//                                    .price(new BigDecimal("200000"))
//                                    .isActive(true)
//                                    .isFeatured(false)
//                                    .isSold(false)
//                                    .build()
//                    );
//
//                    productsRepository.saveAll(products);
//                    log.warn("Sample products have been created");
//                }
//            }
        };
    }
}
