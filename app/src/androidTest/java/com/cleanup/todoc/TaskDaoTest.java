package com.cleanup.todoc;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.DataBase.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class TaskDaoTest {

    // FOR DATA
    private TodocDatabase database;

    // DATA SET FOR TEST
    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Project", 0xFFD67EC3);
    private static Task TASK_VITRES = new Task(1L, PROJECT_ID, "laver les vitres", new Date().getTime());
    private static Task TASK_POUBELLE = new Task(2L, PROJECT_ID, "Vider les poubelles", new Date().getTime());
    private static Task TASK_ASCENC = new Task(3L, PROJECT_ID, "Nettoyer ascenceur", new Date().getTime());


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_VITRES);
        this.database.taskDao().insertTask(TASK_POUBELLE);
        this.database.taskDao().insertTask(TASK_ASCENC);

        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(3,tasks.size());
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);

        Project project = LiveDataTestUtil.getValue(this.database.projectDao().getProjectById(PROJECT_ID));
        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) && project.getId()==PROJECT_ID);
    }

    @Test
    public void insertAndDeleteTasks() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_VITRES);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks()).get(0);
        this.database.taskDao().deleteTask(taskAdded.getId());

        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }




}
