
package CompraPrenda;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class PasosCompra extends Hooks {


    @Test
    public void ordenarRadiantTee() throws InterruptedException{
        
        //busca la imagen con el producto llamado "Radiant Tee" y hace click sobre ella.
        driver.findElement(By.xpath("//img[@alt='Radiant Tee']")).click();
        
        
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(200))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        
        //Espera a que se mustren los elementos.
        WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#qty")));
        elem = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("option-label-size-143-item-169")));
        
        
        //Selecciona el talle "L"
        driver.findElement(By.id("option-label-size-143-item-169")).click();
        
        //Selecciona el color azul
        driver.findElement(By.id("option-label-color-93-item-50")).click();
        
        //Setea la cantidad de unidades a "2"
        driver.findElement(By.cssSelector("#qty")).clear();
        driver.findElement(By.cssSelector("#qty")).sendKeys("2");
        
        //espera a que el boton "Add to cart" se active
        elem=wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("product-addtocart-button"))));
        
        //Hace click en "Add to cart"
        driver.findElement(By.id("product-addtocart-button")).click();
        
        
        //busca el texto que lleva al carrito de compras
        elem=wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@data-bind='html: $parent.prepareMessageForHtml(message.text)']"))));

        List<WebElement> productos= driver.findElements(By.xpath("//a[@href='https://magento.softwaretestingboard.com/checkout/cart/']"));
        WebElement carritoLink=productos.get(0);
        for (WebElement produ : productos){
            if(produ.getText().equals("shopping cart")){
                carritoLink=produ;
            }
        }
        
        
        //Espera a que el texto aparezca en pantalla
        elem=wait.until(ExpectedConditions.elementToBeClickable(carritoLink));
        
        //Hace click en el enlace del carrito
        carritoLink.click();

        //Espera a que la pagina carge completamente (tomando como referencia el total de la compra)
        elem=wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//strong[@data-bind='i18n: title']"))));
        
        //Hace click en "Proceed to checkout"
        driver.findElement(By.xpath("//button[@data-role='proceed-to-checkout']")).click();
        
        //Espera a que carge la pagina (tomando como referencia el casillero de el correo y el boton de "Next")
        elem=wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//button[@data-role='opc-continue']"))));
        elem=wait.until(ExpectedConditions.elementToBeClickable(By.id("customer-email")));
        
        //Carga todos los datos necesarios en el formulario
        driver.findElement(By.id("customer-email")).sendKeys("correoejemplo@gmail.com");
        driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys("Pepito");
        driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys("Sanchez");
        driver.findElement(By.xpath("//input[@name='company']")).sendKeys("Carrefour");
        driver.findElement(By.xpath("//input[@name='street[0]']")).sendKeys("San Marino 244");
        driver.findElement(By.xpath("//input[@name='city']")).sendKeys("San Salvador");
        
        Select estados =new Select(driver.findElement(By.xpath("//select[@name='region_id']")));        
        estados.selectByValue("4");
        
        driver.findElement(By.xpath("//input[@name='postcode']")).sendKeys("12345");
        driver.findElement(By.xpath("//input[@name='telephone']")).sendKeys("4324556");
        
        //seleccionar la primera opcion de envio
        driver.findElement(By.xpath("//input[@name='ko_unique_1']")).click();
        
        //hace click en "Next"
        elem=wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//button[@data-role='opc-continue']"))));
        driver.findElement(By.xpath("//button[@data-role='opc-continue']")).click();
        
        //hacer click en "place order"

        elem=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@title='Place Order']")));
        
        //Hay veces que se intercepta el click por mas de que verifique que el elemento es clickeable, por lo que lo puse en un try-catch
        while(true){
            try{
                driver.findElement(By.xpath("//button[@title='Place Order']")).click();
                break;
            }
            catch(org.openqa.selenium.ElementClickInterceptedException ex){
                continue;
            }
        }
        
        //Espera a que cargue la pagina        
        elem=wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//span[@data-ui-id='page-title-wrapper']"))));
        elem=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='action primary continue']")));
        
        //Se hacen las validaciones
        validaciones();
    }
    
    public void validaciones(){
        
        String [] split= driver.findElement(By.xpath("//div[@class='checkout-success']")).getText().split(" ");
        String orden=split[4].split("\\.")[0];
        
        //Valida que la orden de compra sean solo numeros.
        Assert.assertTrue(orden.matches("\\d+"), "La orden de compra no continen unicamente digitos.");
        
        //Valida que el texto sea “Thank you for purchase!”
        Assert.assertTrue(driver.findElement(By.xpath("//span[@data-ui-id='page-title-wrapper']")).getText().equals("Thank you for purchase!"),"El texto no coincide");
        
        //Valida que el botton "Continue shopping" esté habilitado
        Assert.assertTrue(driver.findElement(By.xpath("//a[@class='action primary continue']")).isEnabled(),"El boton 'Continue shopping' no esta habilitado");
        
        //Valida que el boton "Create an account" esté visible
        Assert.assertTrue(driver.findElement(By.xpath("//a[@class='action primary']")).isDisplayed(),"El boton create an account no está visible");
        
        
    }
}

