package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.model.Task;

import javax.swing.*;

/**
 * Created by Hakim on 14/06/14.
 */
public class TaskListPanel extends JPanel {
    private JList<Task> taskList = new JList<Task>();
    private TaskDetailPanel taskDetailPanel = new TaskDetailPanel();
}
