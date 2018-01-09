package com.example.administrator.pospatrol.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.example.administrator.pospatrol.beans.FormBean;
import com.example.administrator.pospatrol.beans.PosBean;

/**
 * xml解析类，用于xml数据与对象的转换
 *
 * @author Administrator
 *
 */
public class XmlUtils {

    public static List<PosBean> getPos(InputStream in) {
        List<PosBean> posList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "utf-8");
            int type = parser.getEventType();
            PosBean posBean = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                if (type == XmlPullParser.START_DOCUMENT) {
                    posList = new ArrayList<PosBean>();
                } else if (type == XmlPullParser.START_TAG) {
                    if ("item".equals(tagName)) {
                        posBean = new PosBean();
                    } else if ("objNo".equals(tagName)) {
                        posBean.setPosNo(parser.nextText());
                    } else if ("objName".equals(tagName)) {
                        posBean.setPosName(parser.nextText());
                    }
                } else if (type == XmlPullParser.END_TAG) {
                    if ("item".equals(tagName)) {
                        posList.add(posBean);
                    }
                }
                type = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            throw new RuntimeException("xml解析出错");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取xml输入流出错");
        }
        return posList;
    }

    public static List<FormBean> getForms(InputStream in) {
        List<FormBean> forms = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "utf-8");
            int type = parser.getEventType();
            FormBean formBean = null;
            FormBean.FormProject formProject = null;
            List<FormBean.FormProject> projects = null;
            FormBean.FormProject.ProjectResult result = null;
            List<FormBean.FormProject.ProjectResult> results = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                if (type == XmlPullParser.START_DOCUMENT) {
                    forms = new ArrayList<FormBean>();
                } else if (type == XmlPullParser.START_TAG) {
                    if ("btable".equals(tagName)) {
                        formBean = new FormBean();
                        formBean.setId(parser.getAttributeValue(null, "id"));
                        formBean.setName(parser.getAttributeValue(null, "name"));
                        projects = new ArrayList<FormBean.FormProject>();
                    } else if ("bProjects".equals(tagName)) {
                        formProject = new FormBean.FormProject();
                        formProject.setId(parser.getAttributeValue(null, "id"));
                        formProject.setName(parser.getAttributeValue(null,
                                "name"));
                        formProject.setType(parser.getAttributeValue(null,
                                "type"));
                        results = new ArrayList<FormBean.FormProject.ProjectResult>();
                    } else if ("bResult".equals(tagName)) {
                        result = new FormBean.FormProject.ProjectResult();
                        result.setId(parser.getAttributeValue(null, "id"));
                        result.setName(parser.getAttributeValue(null, "name"));
                        result.setValue(parser.getAttributeValue(null, "value"));
                    }
                } else if (type == XmlPullParser.END_TAG) {
                    if ("btable".equals(tagName)) {
                        formBean.setProjects(projects);
                        forms.add(formBean);
                    } else if ("bProjects".equals(tagName)) {
                        formProject.setResults(results);
                        projects.add(formProject);
                    } else if ("bResult".equals(tagName)) {
                        results.add(result);
                    }
                }
                type = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forms;
    }

    public static String fromToXml(List<FormBean> forms) {
        if (forms == null || forms.size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        builder.append("<item>\n");
        for (FormBean form : forms) {
            builder.append("	<btable>\n");
            for (FormBean.FormProject project : form.getProjects()) {
                builder.append("		<bProjects ");
                builder.append("id=\"" + project.getId() + "\" ");
                builder.append("name=\"" + project.getName() + "\" ");
                builder.append("type=\"" + project.getType() + "\">\n");
                for (FormBean.FormProject.ProjectResult result : project
                        .getResults()) {
                    builder.append("			<bResult ");
                    builder.append("id=\"" + result.getId() + "\" ");
                    builder.append("name=\"" + result.getName() + "\" ");
                    builder.append("value=\"" + result.getValue() + "\" />\n");
                }
                builder.append("		</bProjects>\n");
            }
            builder.append("	</btable>\n");
        }

        builder.append("</item>");

        return builder.toString();
    }
}
