package pl.marek.knx.utils;

import java.util.Comparator;

import pl.marek.knx.database.Project;

public class ProjectComparator implements Comparator<Project>{

	public int compare(Project project1, Project project2) {
		return project1.getName().compareTo(project2.getName());
	}
}
