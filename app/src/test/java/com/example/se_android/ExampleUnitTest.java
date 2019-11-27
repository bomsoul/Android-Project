package com.example.se_android;

import com.example.se_android.Models.Quiz;
import com.example.se_android.Models.User;

import org.junit.Test;

import static com.example.se_android.Models.User.getOurInstance;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void answer_should_be_correct() {
        Quiz quiz = new Quiz("ไก่กับไข่อะไรเกิดก่อนกัน","ไก่","ไข่","เกิดพร้อมกัน","ลูกเจี๊ยบ",1);
        assertEquals("ไก่",quiz.getAns());
    }

    @Test
    public void answer_should_be_inCorrect() {
        Quiz quiz = new Quiz("หมากับแมวอะไรน่ารักกว่ากัน?","หมา","แมว","ถูกทุกข้อ","ผิดทุกข้อ",3);
        assertNotEquals("หมา",quiz.getAns());
    }

    @Test
    public void user_static() {
        User user = getOurInstance();
        user.setUsername("User1");
        user.setName("A");
        user.setPassword("1234");
        user.setEmail("user1@mail.com");
        user.setRole("Student");

        User user2 = getOurInstance();

        assertEquals(user.getUsername(),user2.getUsername());
        assertEquals(user.getName(),user2.getName());
        assertEquals(user.getPassword(),user2.getPassword());
        assertEquals(user.getEmail(),user2.getEmail());
        assertEquals(user.getName(),user2.getName());
        assertEquals(user.getRole(),user2.getRole());
    }

    @Test
    public void two_user_should_be_not_the_same() {
        User user = getOurInstance();
        user.setRole("Teacher");

        User user2 = getOurInstance();
        user2.setRole("Student");

        assertNotEquals("Teach",user.getRole());

    }
}
