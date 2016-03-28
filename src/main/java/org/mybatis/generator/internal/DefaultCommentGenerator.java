/*
 *  Copyright 2008 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.Date;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * @author Jeff Butler
 * 
 */
public class DefaultCommentGenerator implements CommentGenerator {

	private Properties properties;
	private boolean suppressDate;
	private boolean suppressAllComments;

	public DefaultCommentGenerator() {
		super();
		properties = new Properties();
		suppressDate = false;
		suppressAllComments = false;
	}

	public void addJavaFileComment(CompilationUnit compilationUnit) {
		// add no file level comments by default
		return;
	}

	/**
	 * Adds a suitable comment to warn users that the element was generated, and
	 * when it was generated.
	 */
	public void addComment(XmlElement xmlElement) {
		if (suppressAllComments) {
			return;
		}
	}

	public void addRootComment(XmlElement rootElement) {
		// add no document level comments by default
		return;
	}

	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);

		suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

		suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
	}

	/**
	 * This method adds the custom javadoc tag for. You may do nothing if you do
	 * not wish to include the Javadoc tag - however, if you do not include the
	 * Javadoc tag then the Java merge capability of the eclipse plugin will
	 * break.
	 * 
	 * @param javaElement
	 *            the java element
	 */
	protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
		javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
		StringBuilder sb = new StringBuilder();
		sb.append(" * "); //$NON-NLS-1$
		sb.append(MergeConstants.NEW_ELEMENT_TAG);
		if (markAsDoNotDelete) {
			sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
		}
		String s = getDateString();
		if (s != null) {
			sb.append(' ');
			sb.append(s);
		}
		javaElement.addJavaDocLine(sb.toString());
	}

	/**
	 * This method returns a formated date string to include in the Javadoc tag
	 * and XML comments. You may return null if you do not want the date in
	 * these documentation elements.
	 * 
	 * @return a string representing the current timestamp, or null
	 */
	protected String getDateString() {
		if (suppressDate) {
			return null;
		} else {
			return new Date().toString();
		}
	}

	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
	}

	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, String comment) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		String remark = comment;
		if (remark != null && remark.length() != 0) {
			innerClass.addJavaDocLine("/**"); //$NON-NLS-1$
			sb.append(" * "); //$NON-NLS-1$
			remark = remark.replaceAll(OutputUtilities.lineSeparator,
					"<br>" + OutputUtilities.lineSeparator + "\t * ");
			sb.append(remark);
			innerClass.addJavaDocLine(sb.toString());
			innerClass.addJavaDocLine(" */"); //$NON-NLS-1$
		}
	}

	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		innerEnum.addJavaDocLine("/**"); //$NON-NLS-1$
		innerEnum.addJavaDocLine(" * This enum was generated by MyBatis Generator."); //$NON-NLS-1$

		sb.append(" * This enum corresponds to the database table "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerEnum.addJavaDocLine(sb.toString());

		addJavadocTag(innerEnum, false);

		innerEnum.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		String remark = introspectedColumn.getRemarks();
		if (remark != null && remark.length() != 0) {
			field.addJavaDocLine("/**"); //$NON-NLS-1$
			sb.append(" * "); //$NON-NLS-1$
			remark = remark.replaceAll(OutputUtilities.lineSeparator,
					"<br>" + OutputUtilities.lineSeparator + "\t * ");
			sb.append(remark);
			field.addJavaDocLine(sb.toString());
			field.addJavaDocLine(" */"); //$NON-NLS-1$
		}
	}

	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

	}

	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

	}

	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable, String comments) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		String remark = comments;
		if (remark != null && remark.length() != 0) {
			method.addJavaDocLine("/**"); //$NON-NLS-1$
			sb.append(" * "); //$NON-NLS-1$
			remark = remark.replaceAll(OutputUtilities.lineSeparator,
					"<br>" + OutputUtilities.lineSeparator + "\t * ");
			sb.append(remark);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */"); //$NON-NLS-1$
		}
	}

	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		String remark = introspectedColumn.getRemarks();

		if (remark != null && remark.length() != 0) {
			method.addJavaDocLine("/**");
			sb.append(" * @return ");
			remark = remark.replaceAll(OutputUtilities.lineSeparator,
					"<br>" + OutputUtilities.lineSeparator + "\t *         ");

			sb.append(remark);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */");
		}
	}

	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		String remark = introspectedColumn.getRemarks();

		if (remark != null && remark.length() != 0) {
			method.addJavaDocLine("/**");
			sb.append(" * @param " + JavaBeansUtil.getCamelCaseString(introspectedColumn.getActualColumnName(), false) + " "); //$NON-NLS-1$
			remark = remark.replaceAll(OutputUtilities.lineSeparator,
					"<br>" + OutputUtilities.lineSeparator + "\t *            ");
			sb.append("" + OutputUtilities.lineSeparator + "\t *            " + remark);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */"); //$NON-NLS-1$
		}
	}

	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (suppressAllComments) {
			return;
		}
	}

	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete,
			String comment) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		String remark = comment;
		if (remark != null && remark.length() != 0) {
			innerClass.addJavaDocLine("/**"); //$NON-NLS-1$
			sb.append(" * "); //$NON-NLS-1$
			remark = remark.replaceAll(OutputUtilities.lineSeparator,
					"<br>" + OutputUtilities.lineSeparator + "\t * ");
			sb.append(remark);
			innerClass.addJavaDocLine(sb.toString());
			innerClass.addJavaDocLine(" */"); //$NON-NLS-1$
		}
	}
}
