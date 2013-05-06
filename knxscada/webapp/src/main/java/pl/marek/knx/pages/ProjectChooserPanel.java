package pl.marek.knx.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.components.ProjectItem;
import pl.marek.knx.database.Project;
import pl.marek.knx.pages.DialogsPanel.DialogType;

@HtmlFile("projectchooser.html")
public class ProjectChooserPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private Button newProjectButton;
	private Button closeButton;

	private List<Project> projects;
	private RepeatingView projectsView;

	private static boolean execute;

	public ProjectChooserPanel(String componentName, DBManager dbManager) {
		super(componentName, dbManager);
		setOutputMarkupId(true);
		execute = true;
		projects = new ArrayList<Project>(dbManager.getAllProjects());
		loadComponents();
	}

	private void loadComponents() {
		removeAll();

		projectsView = new RepeatingView("project");
		for (Project project : projects) {
			ProjectItem item = new ProjectItem(projectsView.newChildId(),
					new Model<Project>(project));

			PopupMenu menu = item.getPopupMenu();
			PopupMenuItem editMenu = menu.createPopupMenuItem(getString("project.edit.menuitem"), "images/edit_icon.png", true);
			PopupMenuItem deleteMenu = menu.createPopupMenuItem(getString("project.remove.menuitem"), "images/trash_icon.png", true);

			editMenu.add(new ProjectItemEditClickBehavior(project));
			deleteMenu.add(new ProjectItemDeleteClickBehavior(project));
			
			item.add(new ProjectItemClickBehavior(project));
			
			projectsView.add(item);
		}
		add(projectsView);
		

		closeButton = new Button("projectChooserCloseButton") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				Project project = getCurrentProject();
				if(project == null){
					tag.put("style", "display: none;");
				}
			}
		};
		closeButton.add(new AjaxEventBehavior("click") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				target.appendJavaScript("hideProjectChooser();");
			}
		});

		add(closeButton);

		newProjectButton = new Button("newProjectButton");
		newProjectButton.add(new AjaxEventBehavior("click") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				DialogsPanel dialogs = getDialogsPanel();
				dialogs.setType(DialogType.NEW_PROJECT);
				dialogs.setDialogPanel(new CreateEditProjectFormPanel("dialog", getDBManager()));
				target.add(dialogs);
				target.appendJavaScript("initProjectDialog('"+ getString("new-project") + "','"+getString("cancel")+"'); showDialog();");
			}
		});
		add(newProjectButton);

	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		Project project = getCurrentProject();
		if (project == null || isContainsParameter("project-form-submit-button")) {
			tag.put("style", "display: block;");
		}
		tag.put("onload", "resize();");
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
		loadComponents();
	}

	private class ProjectItemClickBehavior extends AjaxEventBehavior {

		private static final long serialVersionUID = 1L;

		private Project project;

		public ProjectItemClickBehavior(Project project) {
			super("click");
			this.project = project;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			if(execute){
				PageParameters params = new PageParameters();
				int projectId = project.getId();
				params.add("project", projectId);
	
				setResponsePage(Index.class, params);
			}
			execute = true;
		}
	};

	private class ProjectItemEditClickBehavior extends AjaxEventBehavior {

		private static final long serialVersionUID = 1L;

		private Project project;

		public ProjectItemEditClickBehavior(Project project) {
			super("click");
			this.project = project;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			execute = false;

			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.EDIT_PROJECT);
			dialogs.setDialogPanel(new CreateEditProjectFormPanel("dialog", getDBManager(), project));
			target.add(dialogs);
			target.appendJavaScript("initProjectDialog('"+ getString("edit-project") + "', '"+getString("cancel")+"'); showDialog();");
			
		}
	}

	private class ProjectItemDeleteClickBehavior extends AjaxEventBehavior {

		private static final long serialVersionUID = 1L;

		private Project project;

		public ProjectItemDeleteClickBehavior(Project project) {
			super("click");
			this.project = project;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			execute = false;

			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.DELETE_PROJECT);
			dialogs.setDialogPanel(new DeleteFormPanel("dialog", getDBManager(), project));
			target.add(dialogs);
			target.appendJavaScript("initRemoveDialog('"+ getString("yes") + "', '"+getString("no")+"'); showDialog();");
			
		}
	}
	
	public String getProjectItemIdByProject(Project project){
		String projectId = "";
		for(int i=0;i<projectsView.size();i++){
			ProjectItem item = (ProjectItem)projectsView.get(i);
			if(item.getProject().getId() == project.getId()){
				projectId = item.getMarkupId();
				break;
			}
		}
		return projectId;
	}

}
