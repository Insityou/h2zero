package synapticloop.sample.h2zero.taglib.user;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//          (java-create-taglib-counter-count-all.templar)

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import synapticloop.sample.h2zero.counter.UserCounter;
import synapticloop.sample.h2zero.model.util.Constants;

public class CountAllTag extends BodyTagSupport {
	private static final String BINDER = Constants.USER_binder;

	private static final Logger LOGGER = Logger.getLogger(CountAllTag.class);

	private String var = null;
	private boolean removeVar = false;

	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, UserCounter.countAllSilent());
		return(EVAL_BODY_INCLUDE);
}

	public int doEndTag() throws JspException {
		pageContext.removeAttribute(var, PageContext.PAGE_SCOPE);
		return(EVAL_PAGE);
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setRemoveVar(boolean removeVar) {
		this.removeVar = removeVar;
	}

	public boolean getRemoveVar() {
		return removeVar;
	}
}