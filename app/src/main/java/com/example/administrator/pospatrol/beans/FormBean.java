package com.example.administrator.pospatrol.beans;

import java.util.List;

/**
 * 表单对象 btable
 *
 * @author Administrator
 *
 */
public class FormBean {
    private String id;
    private String name;
    private List<FormProject> projects;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FormProject> getProjects() {
        return projects;
    }

    public void setProjects(List<FormProject> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "FormBean [id=" + id + ", name=" + name + ", projects="
                + projects + "]";
    }

    /**
     * 检查对象 bProjects
     *
     * @author Administrator
     *
     */
    public static class FormProject {
        public static final String TYPE_RADIO = "radio";
        public static final String TYPE_CHECKBOX = "checkbox";
        public static final String TYPE_INPUT = "input";
        private String id;
        private String name;
        private String type;
        private List<ProjectResult> results;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ProjectResult> getResults() {
            return results;
        }

        public void setResults(List<ProjectResult> results) {
            this.results = results;
        }

        @Override
        public String toString() {
            return "FormProject [id=" + id + ", name=" + name + ", type="
                    + type + ", results=" + results + "]";
        }

        /**
         * 检查取值 bResult
         *
         * @author Administrator
         *
         */
        public static class ProjectResult {
            private String id;
            private String name;
            private String value;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "ProjectResult [id=" + id + ", name=" + name
                        + ", value=" + value + "]";
            }

        }
    }
}
