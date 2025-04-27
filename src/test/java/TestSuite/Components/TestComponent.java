package TestSuite.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

public class TestComponent extends Component {
    private String testField;

    public TestComponent() {

    }

    public TestComponent(String testField) {
        this.testField = testField;
    }

    public String getTestField() {
        return testField;
    }
    public void setTestField(String testField) {
        this.testField = testField;
    }
}
