package au.com.nab.icommerce.product.command.service.impl;

import au.com.nab.icommerce.common.error.ErrorCode;
import au.com.nab.icommerce.product.command.domain.Product;
import au.com.nab.icommerce.product.command.mapper.ProductMapper;
import au.com.nab.icommerce.product.command.repository.ProductRepository;
import au.com.nab.icommerce.product.command.service.ProductService;
import au.com.nab.icommerce.product.protobuf.PProduct;
import com.google.protobuf.Int32Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    public Int32Value createProduct(PProduct pProduct) {
        int response = ErrorCode.FAILED;
        try {
            Product product = productMapper.toDomain(pProduct);
            product = productRepository.save(product);
            if (product.getId() > 0) {
                response = product.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Int32Value.of(response);
    }

    @Override
    public Int32Value updateProduct(PProduct pProduct) {
        int response = ErrorCode.FAILED;
        try {
            Product product = productMapper.toDomain(pProduct);
            productRepository.save(product);
            response = ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Int32Value.of(response);
    }

}
