package xl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @说明 ： 自定义注解 - 注解方法名
 * @作者 ：WangXL
 * @时间 ：2018 8 21
 **/
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface WSocketMethod {
	String value();
}
