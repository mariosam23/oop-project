package app.pages;

/**
 * The interface Page.
 */
public abstract class Page {
    /**
     * Print current page string.
     *
     * @return the current page string
     */
    public abstract String printCurrentPage();

    /**
     * @return the type of the page
     */
    public abstract String pageType();

    /**
     * @return the owner of the page
     */
    public abstract String pageOwner();
}
