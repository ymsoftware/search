package org.ap.core.search;

import org.ap.core.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymetelkin on 5/5/16.
 */
public class QueryStringParser {
    private char[] chars;
    private int position;
    private boolean isField;
    private String fieldName;
    private char last;
    private List<String> list;
    private StringBuilder current;

    public String parse(String query) {
        query = Helpers.safeTrim(query);
        if (query == null) {
            return query;
        }

        this.chars = query.toCharArray();
        this.position = 0;
        this.list = new ArrayList<>();
        this.current = new StringBuilder();

        while (this.position < this.chars.length) {
            char chr = next();

            switch (chr) {
                case '"':
                    parseQuotes();
                    break;
                case ':':
                    parseFieldName();
                    break;
                case ' ':
                    parseWS();
                    break;
                case '\n':
                case '\t':
                case '\r':
                    chr = ' ';
                    parseWS();
                    break;
                case '[':
                case '{':
                    parseRange(chr);
                    break;
                case '/':
                    parseRegex();
                    break;
                case '(':
                    parseGroup();
                    break;
                default:
                    processChar(chr);
            }

            this.last = chr;
        }

        addValue();

        return String.join("", this.list);
    }

    private void parseQuotes() {
        boolean isEnd = false;

        StringBuilder sb = new StringBuilder();

        while (!isEnd) {
            char chr = next();
            if (chr == '"') {
                int idx = sb.length() - 1;
                if (sb.charAt(idx) == '\\') {
                    sb.deleteCharAt(idx);
                    sb.append(chr);
                } else {
                    isEnd = true;
                }
            } else {
                sb.append(chr);
            }
        }

        String value = String.format("\"%s\"", sb.toString());

        if (this.isField) {
            addField(value);
        } else {
            this.list.add(value);
        }
    }

    private void parseWS() {
        if (this.last != ' ' && !this.isField) {
            addValue();
            this.list.add(" ");
        }
    }

    private void parseRange(char start) {
        if (this.isField) {
            String from = null;
            String join = null;
            String to = null;
            StringBuilder sb = new StringBuilder();
            boolean done = false;

            while (this.position < this.chars.length && !done) {
                char chr = next();

                switch (chr) {
                    case ' ':
                        if (from == null && sb.length() > 0) {
                            from = sb.toString();
                            sb = new StringBuilder();
                        } else if (from != null && join == null && sb.length() > 0) {
                            join = sb.toString();
                            sb = new StringBuilder();
                        }
                        break;
                    case ']':
                    case '}':
                        if (sb.length() > 0) {
                            to = sb.toString();
                        }
                        if (from != null && join.equals("TO") && to != null) {
                            String value = String.format("%c%s %s %s%c", start, from, join, to, chr);
                            addField(value);
                            return;
                        } else {
                            done = true;
                        }
                        break;
                    default:
                        sb.append(chr);
                }
            }

            addField();

            this.list.add(" ");
            if (from != null) {
                this.list.add(from);
            }
            if (join != null) {
                this.list.add(" ");
                this.list.add(join);
                this.list.add(" ");
            }
            if (to != null) {
                this.list.add(to);
            }
        } else {
            this.list.add(String.valueOf(start));
        }
    }

    private void parseRegex() {
        if (this.isField) {
            StringBuilder sb = new StringBuilder();

            while (this.position < this.chars.length) {
                char chr = next();

                switch (chr) {
                    case '/':
                        if (sb.length() > 0) {
                            String value = String.format("/%s/", sb.toString());
                            addField(value);
                            return;
                        } else {
                            sb.append(chr);
                        }
                        break;
                    default:
                        sb.append(chr);
                }
            }

            addField();

            if (sb.length() > 0) {
                this.list.add(" /");
                this.list.add(sb.toString());
            }
        } else {
            this.list.add("/");
        }
    }

    private void parseGroup() {
        if (this.isField) {
            int count = 0;
            StringBuilder sb = new StringBuilder();
            sb.append('(');

            while (this.position < this.chars.length) {
                char chr = next();

                switch (chr) {
                    case '(':
                        count++;
                        break;
                    case ')':
                        if (count == 0) {
                            sb.append(chr);
                            addField(sb.toString());
                            return;
                        } else {
                            count--;
                        }
                        break;
                }

                sb.append(chr);
            }

            addField();

            if (sb.length() > 0) {
                this.list.add(" ");
                this.list.add(sb.toString());
            }
        } else {
            this.list.add("(");
        }
    }

    private void parseFieldName() {
        boolean success = true;
        boolean addSpace = true;
        String name = null;

        if (this.current.length() > 0) {
            name = this.current.toString();
            this.current = new StringBuilder();
        } else if (this.list.size() > 1) {
            name = this.list.get(this.list.size() - 2);
            addSpace = false;
        }

        name = Helpers.safeTrim(name);
        if (name != null) {
            char[] chars = name.toCharArray();
            int i = 0;
            boolean specail = true;

            while (i < chars.length) {
                char chr = chars[i++];
                switch (chr) {
                    case '.':
                    case '_':
                        if (specail) {
                            success = false;
                            break;
                        } else {
                            specail = true;
                        }
                    default:
                        if (Character.isAlphabetic(chr)) {
                            specail = false;
                        } else {
                            success = false;
                            break;
                        }
                }
            }
        }

        if (success) {
            this.isField = true;
            this.fieldName = name;

            if (!addSpace) {
                list.remove(list.size() - 1);
                list.remove(list.size() - 1);
            }
        } else if (addSpace) {
            this.list.add(name);
            this.list.add(" ");
        }
    }

    private void processChar(char chr) {
        this.current.append(chr);
    }

    private char next() {
        return this.chars[this.position++];
    }

    private String last() {
        int size = this.list.size();
        return size == 0 ? "" : this.list.get(size - 1);
    }

    private void addField(String value) {
        this.list.add(this.fieldName);
        this.list.add(":");
        this.list.add(value);
        this.isField = false;
        this.fieldName = null;
    }

    private void addValue() {
        if (this.current.length() > 0) {
            if (this.isField) {
                addField(this.current.toString());
            } else {
                this.list.add(this.current.toString());
            }
            this.current = new StringBuilder();
        }
    }

    private void addField() {
        this.list.add(this.fieldName);
        this.isField = false;
        this.fieldName = null;
    }
}
