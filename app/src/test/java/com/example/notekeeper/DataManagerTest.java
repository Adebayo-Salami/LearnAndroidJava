package com.example.notekeeper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {
    static DataManager sDataManager;

    @BeforeClass
    public static void setUpClass() throws Exception {
        sDataManager = DataManager.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();
    }

    @Test
    public void createNewNote() {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body of my second test note";

        int noteIndex = sDataManager.createNewNote();
        NoteInfo newNote = sDataManager.getNotes().get(noteIndex);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);
        newNote.setCourse(course);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertSame(newNote, compareNote);   //assertSame(ExpectedValue, ComparedValue);
        assertEquals(course, newNote.getCourse());
        assertEquals(noteText, newNote.getText());
        assertEquals(noteTitle, newNote.getTitle());
    }

    @Test
    public void findSimilarNotes() throws Exception {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText1 = "This is the body text of my test note";
        final String noteText2 = "This is the body of my second test note";

        int noteIndex1 = sDataManager.createNewNote();
        NoteInfo newNote1 = sDataManager.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        int noteIndex2 = sDataManager.createNewNote();
        NoteInfo newNote2 = sDataManager.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);

        int foundIndex1 = sDataManager.findNote(newNote1);
        assertEquals(noteIndex1, foundIndex1);

        int foundIndex2 = sDataManager.findNote(newNote2);
        assertEquals(noteIndex2, foundIndex2);
    }

    @Test
    public void createNewNoteInstantly() throws Exception {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body text of my test note";
        NoteInfo newNote = new NoteInfo(course, noteTitle, noteText);

        NoteInfo createdNote = sDataManager.createNewNote(newNote);
        int createdNoteIndex = sDataManager.findNote(createdNote);
        NoteInfo compareCreatedNote = sDataManager.getNotes().get(createdNoteIndex);

        assertEquals(course, createdNote.getCourse());
        assertEquals(noteTitle, createdNote.getTitle());
        assertEquals(noteText, createdNote.getText());

        assertEquals(createdNote.getCourse(), compareCreatedNote.getCourse());
        assertEquals(createdNote.getTitle(), compareCreatedNote.getTitle());
        assertEquals(createdNote.getText(), compareCreatedNote.getText());
        assertSame(createdNote, compareCreatedNote);
    }
}