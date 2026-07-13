package com.alzaidan.inventory.config;

import com.alzaidan.inventory.entity.Product;
import com.alzaidan.inventory.entity.User;
import com.alzaidan.inventory.repository.ProductRepository;
import com.alzaidan.inventory.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
    }

    private void seedUsers() {

        if (userRepository.count() > 0) {
            return;
        }

        log.info("Seeding demo users...");

        userRepository.save(User.builder()
                .username("admin")
                .name("Abubakar Zaidan")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMINISTRATOR)
                .enabled(true)
                .build());

        userRepository.save(User.builder()
                .username("sales")
                .name("Fatima Bello")
                .password(passwordEncoder.encode("sales123"))
                .role(User.Role.SALES_MANAGER)
                .enabled(true)
                .build());

        userRepository.save(User.builder()
                .username("inventory")
                .name("Ibrahim Musa")
                .password(passwordEncoder.encode("inv123"))
                .role(User.Role.INVENTORY_OFFICER)
                .enabled(true)
                .build());

        log.info("Demo users seeded.");
    }

    private void seedProducts() {

        if (productRepository.count() > 0) {
            return;
        }

        log.info("Seeding demo products...");

        Object[][] products = {
                {"Samsung Galaxy A54", "ELEC-001", "ELECTRONICS", 240000, 185000, 12, 10, 50, "Pcs", "TechZone Lagos"},
                {"Basmati Rice (25kg)", "FOOD-001", "FOOD_AND_BEV", 17500, 13000, 58, 15, 100, "Bags", "Kwara Farms Ltd"},
                {"Nasco Standing Fan", "ELEC-002", "ELECTRONICS", 38000, 28000, 2, 8, 40, "Pcs", "Nasco Nigeria"},
                {"Men's Ankara 3-Piece", "CLTH-001", "CLOTHING", 15000, 9000, 24, 10, 80, "Sets", "Balogun Market"},
                {"Indomie Noodles (Ctn)", "FOOD-002", "FOOD_AND_BEV", 5500, 4200, 120, 30, 200, "Cartons", "Dufil Nigeria"},
                {"Hisense 32\" LED TV", "ELEC-003", "ELECTRONICS", 195000, 148000, 3, 5, 25, "Pcs", "TechZone Lagos"},
                {"Omo Detergent (5kg)", "HHLD-001", "HOUSEHOLD", 4800, 3600, 0, 20, 100, "Packs", "Unilever Dist."},
                {"Groundnut Oil (25L)", "FOOD-003", "FOOD_AND_BEV", 28000, 21000, 35, 10, 60, "Kegs", "Kwara Farms Ltd"}
        };

        for (Object[] p : products) {

            Product product = Product.builder()
                    .name((String) p[0])
                    .sku((String) p[1])
                    .category(Product.Category.valueOf((String) p[2]))
                    .price(BigDecimal.valueOf(((Number) p[3]).longValue()))
                    .cost(BigDecimal.valueOf(((Number) p[4]).longValue()))
                    .stock((Integer) p[5])
                    .minStock((Integer) p[6])
                    .maxStock((Integer) p[7])
                    .unit((String) p[8])
                    .supplier((String) p[9])
                    .build();

            productRepository.save(product);
        }

        log.info("Demo products seeded.");
    }
}