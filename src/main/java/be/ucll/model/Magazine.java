package be.ucll.model;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class Magazine extends Publication {

    @NotBlank(message = "Editor is required")
    private String editor;

    @NotBlank(message = "ISSN is required")
    private String issn;

    public Magazine(String title, String editor, String issn, int pubYear, int availableCopies) {
        super(title, pubYear, availableCopies);
        setEditor(editor);
        setIssn(issn);

    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    @Override
    public String toString() {
        return super.toString() + " " + "Magazine{" +
                "editor='" + editor + '\'' +
                ", issn='" + issn + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Magazine magazine = (Magazine) o;
        return Objects.equals(editor, magazine.editor) && Objects.equals(issn, magazine.issn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), editor, issn);
    }
}
