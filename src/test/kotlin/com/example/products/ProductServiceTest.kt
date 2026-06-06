package com.example.products

import com.example.departments.TestData
import com.example.exception.NotFoundException
import com.example.exception.ValidationException
import com.example.model.entity.Product
import com.example.model.repository.ProductRepository
import com.example.products.dto.CreateProductRequest
import com.example.products.dto.UpdateProductRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ProductServiceTest {

    private val productRepository =
        mockk<ProductRepository>()

    private val service =
        ProductService(
            productRepository
        )

    @Test
    fun `create throws ValidationException when name is blank`() = runTest {

        assertFailsWith<ValidationException> {

            service.create(
                CreateProductRequest(
                    name = "",
                    storeId = 1
                )
            )
        }
    }

    @Test
    fun `create throws ValidationException when price is negative`() = runTest {

        assertFailsWith<ValidationException> {

            service.create(
                CreateProductRequest(
                    name = "Milk",
                    storeId = 1,
                    price = -10.0
                )
            )
        }
    }

    @Test
    fun `create returns created product`() = runTest {

        coEvery {
            productRepository.addProduct(any())
        } returns TestData.product()

        val result =
            service.create(
                CreateProductRequest(
                    name = "Milk",
                    storeId = 1,
                    price = 10.0
                )
            )

        assertEquals(
            "Milk",
            result.name
        )

        coVerify(exactly = 1) {
            productRepository.addProduct(any())
        }
    }

    @Test
    fun `getById throws NotFoundException when product does not exist`() = runTest {

        coEvery {
            productRepository.productById(1)
        } returns null

        assertFailsWith<NotFoundException> {

            service.getById(1)
        }
    }

    @Test
    fun `getById returns product`() = runTest {

        coEvery {
            productRepository.productById(1)
        } returns TestData.product()

        val result =
            service.getById(1)

        assertEquals(
            "Milk",
            result.name
        )
    }

    @Test
    fun `getByStore returns mapped products`() = runTest {

        coEvery {
            productRepository.productsByStoreId(1)
        } returns listOf(
            TestData.product()
        )

        val result =
            service.getByStore(1)

        assertEquals(
            1,
            result.size
        )

        assertEquals(
            "Milk",
            result.first().name
        )
    }

    @Test
    fun `update throws ValidationException when name is blank`() = runTest {

        assertFailsWith<ValidationException> {

            service.update(
                UpdateProductRequest(
                    articleNo = 1,
                    name = "",
                    storeId = 1
                )
            )
        }
    }

    @Test
    fun `update throws ValidationException when price is negative`() = runTest {

        assertFailsWith<ValidationException> {

            service.update(
                UpdateProductRequest(
                    articleNo = 1,
                    name = "Milk",
                    storeId = 1,
                    price = -1.0
                )
            )
        }
    }

    @Test
    fun `update returns updated product`() = runTest {

        coEvery {
            productRepository.updateProduct(any())
        } returns TestData.product()

        val result =
            service.update(
                UpdateProductRequest(
                    articleNo = 1,
                    name = "Milk",
                    storeId = 1,
                    price = 10.0
                )
            )

        assertEquals(
            "Milk",
            result.name
        )
    }

    @Test
    fun `delete throws NotFoundException when product does not exist`() = runTest {

        coEvery {
            productRepository.removeProductById(1)
        } returns false

        assertFailsWith<NotFoundException> {

            service.delete(1)
        }
    }

    @Test
    fun `delete removes product`() = runTest {

        coEvery {
            productRepository.removeProductById(1)
        } returns true

        service.delete(1)

        coVerify(exactly = 1) {
            productRepository.removeProductById(1)
        }
    }
}