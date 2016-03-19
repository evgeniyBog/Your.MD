package com.yourmd.search.dictionary;

import com.yourmd.search.Application;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DictionaryTest extends TestCase {

    @Autowired
    private Dictionary dictionary;

    @Test
    public void testGetIndexOf() throws Exception {
        Assert.assertNotNull(dictionary);
        Assert.assertTrue(dictionary.getIndexOf("HTN") == 1);
        Assert.assertTrue(dictionary.getIndexOf("hypertenssion") == 16);
    }

    @Test
    public void testGetValue() throws Exception {
        Assert.assertNotNull(dictionary);
        Assert.assertTrue(dictionary.getValue(1).equals("HTN"));
        Assert.assertTrue(dictionary.getValue(16).equals("hypertenssion"));
    }

    @Test
    public void testSingleton() {
        Dictionary dict1 = Dictionary.getInstance();
        Dictionary dict2 = Dictionary.getInstance();
        assertEquals(dict1, dict2);
        assertEquals(dict1, dictionary);
    }
}