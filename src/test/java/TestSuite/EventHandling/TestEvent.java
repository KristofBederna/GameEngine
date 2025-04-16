package TestSuite.EventHandling;

import inf.elte.hu.gameengine_javafx.Misc.EventHandling.Event;

import java.util.ArrayList;

public class TestEvent implements Event {
    private String testString = "test";
    private ArrayList<String> testList;
    public TestEvent(ArrayList<String> test) {
        testList = test;
    }

    public String getTestString() {
        return testString;
    }

    public ArrayList<String> getTestList() {
        return testList;
    }
}
