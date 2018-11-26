package org.nerve.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * org.nerve.tonglian.domain
 * Created by zengxm on 2017/8/22.
 */
public class IdEntityTest {

	DateEntity dateEntity = new DateEntity();

	@Test
	public void eqTest(){
		assertNotEquals(null, dateEntity);
		assertNotEquals("Hello", dateEntity);
		assertNotEquals(dateEntity, new DateEntity());
		assertEquals(dateEntity, dateEntity);
	}
}
