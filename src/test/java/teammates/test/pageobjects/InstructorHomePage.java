package teammates.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;



public class InstructorHomePage extends AppPage {
    
    @FindBy(id = "searchbox")
    private WebElement searchBox;
    
    @FindBy(id = "button_search")
    private WebElement searchButton;
    
    @FindBy(id = "sortbyid")
    private WebElement sortByIdButton;

    @FindBy(id = "sortbyname")
    private WebElement sortByNameButton;
    
    @FindBy(id = "sortbydate")
    private WebElement sortByDateButton;
    
    @FindBy(className = "t_course_enroll0")
    private WebElement courseEnrollLink;
    
    @FindBy(className = "t_course_view0")
    private WebElement courseViewLink;
    
    @FindBy(className = "t_course_edit0")
    private WebElement courseEditLink;
    
    @FindBy(className = "t_course_add_eval0")
    private WebElement courseAddEvaluationLink;
    
    @FindBy(className = "t_session_view0")
    private WebElement sessionViewResultsLink;
    
    @FindBy(className = "t_session_edit0")
    private WebElement sessionEditLink;
    
    @FindBy(className = "t_session_preview0")
    private WebElement sessionPreviewLink;
    
    public InstructorHomePage(Browser browser){
        super(browser);
    }

    @Override
    protected boolean containsExpectedPageContents() {
        return containsExpectedPageContents(getPageSource());
    }
    
    public static boolean containsExpectedPageContents(String pageSource){
        return pageSource.contains("<h1>Instructor Home</h1>");
    }

    public InstructorHelpPage clickHelpLink() {
        instructorHelpTab.click();
        waitForPageToLoad();
        switchToNewWindow();
        return changePageType(InstructorHelpPage.class);
    }
    
    public void clickSortByIdButton() {
        sortByIdButton.click();
        waitForPageToLoad();
    }
    
    public void clickSortByNameButton() {
        sortByNameButton.click();
        waitForPageToLoad();
    }
    
    public void clickSortByDateButton() {
        sortByDateButton.click();
        waitForPageToLoad();
    }
    
    public InstructorCourseEnrollPage clickCourseErollLink() {
        courseEnrollLink.click();
        waitForPageToLoad();
        return changePageType(InstructorCourseEnrollPage.class);
    }
    
    public InstructorCourseDetailsPage clickCourseViewLink() {
        courseViewLink.click();
        waitForPageToLoad();
        return changePageType(InstructorCourseDetailsPage.class);
    }
    
    public InstructorCourseEditPage clickCourseEditLink() {
        courseEditLink.click();
        waitForPageToLoad();
        return changePageType(InstructorCourseEditPage.class);
    }
    
    public InstructorEvalsPage clickCourseAddEvaluationLink() {
        courseAddEvaluationLink.click();
        waitForPageToLoad();
        return changePageType(InstructorEvalsPage.class);
    }
    
    public InstructorEvalResultsPage clickSessionViewResultsLink() {
        sessionViewResultsLink.click();
        waitForPageToLoad();
        return changePageType(InstructorEvalResultsPage.class);
    }
    
    public InstructorEvalEditPage clickSessionEditLink() {
        sessionEditLink.click();
        waitForPageToLoad();
        return changePageType(InstructorEvalEditPage.class);
    }
    
    public InstructorEvalPreview clickSessionPreviewLink() {
        sessionPreviewLink.click();
        waitForPageToLoad();
        switchToNewWindow();
        return changePageType(InstructorEvalPreview.class);
    }
    
    public void clickHomeTab() {
        instructorHomeTab.click();
        waitForPageToLoad();
    }
    
    public InstructorStudentListPage searchForStudent(String studentName){
        searchBox.clear();
        searchBox.sendKeys(studentName);
        searchButton.click();
        waitForPageToLoad();
        return changePageType(InstructorStudentListPage.class);
    }
    
    public WebElement getViewResponseLink(String courseId, String evalName) {
        int evaluationRowId = getEvaluationRowId(courseId, evalName);
        String xpathExp = "//tr[@id='session"+ evaluationRowId +"']/td[contains(@class,'t_session_response')]/a";

        return browser.driver.findElement(By.xpath(xpathExp));
    }
    
    public void setViewResponseLinkValue(WebElement element, String newValue) {
        JavascriptExecutor js = (JavascriptExecutor) browser.driver; 
        js.executeScript("arguments[0].href=arguments[1]", element, newValue );
    }

    public WebElement getRemindLink(String courseId, String evalName) {
        return getLinkInRow("t_session_remind", getEvaluationRowId(courseId, evalName));
    }
    
    public WebElement getPublishLink(String courseId, String evalName){
        return getLinkInRow("t_session_publish", getEvaluationRowId(courseId, evalName));
    }
    
    public WebElement getUnpublishLink(String courseId, String evalName){
        return getLinkInRow("t_session_unpublish", getEvaluationRowId(courseId, evalName));
    }
    
    public WebElement getDeleteEvalLink(String courseId, String evalName){
        return getLinkInRow("t_session_delete", getEvaluationRowId(courseId, evalName));
    }
    
    public WebElement getDeleteCourseLink(String courseId){
        return getLinkInRow("t_course_delete", getCourseRowId(courseId));
    }
    
    public InstructorHomePage clickArchiveCourseLink(String courseId){
        getLinkInRow("t_course_archive", getCourseRowId(courseId)).click();
        waitForPageToLoad();
        return this;
    }
    
    private WebElement getLinkInRow(String elementClassNamePrefix, int rowId){
        return browser.driver.findElement(By.className(elementClassNamePrefix + rowId));
    }

    private int getEvaluationRowId(String courseId, String evalName) {
        int courseRowID = getCourseRowId(courseId);
        if (courseRowID == -1)
            return -2;
        String template = "//div[@id='course%d']//table[@class='dataTable']//tr[@id='session%d']";
        int max = (Integer) (browser.selenium)
                .getXpathCount("//div//table[@class='dataTable']//tr");
        for (int id = 0; id < max; id++) {
            if (getElementText(
                    By.xpath(String.format(template + "//td[1]", courseRowID,
                            id))).equals(evalName)) {
                return id;
            }
        }
        return -1;
    }
    
    private int getCourseRowId(String courseId) {
        int id = 0;
        while (isElementPresent(By.id("course" + id))) {
            if (getElementText(
                    By.xpath("//div[@id='course" + id
                            + "']/div[@class='result_homeTitle']/h2"))
                    .startsWith("[" + courseId + "]")) {
                return id;
            }
            id++;
        }
        return -1;
    }
    
    private String getElementText(By locator) {
        if (!isElementPresent(locator))
            return "";
        return browser.driver.findElement(locator).getText();
    }
    

}
