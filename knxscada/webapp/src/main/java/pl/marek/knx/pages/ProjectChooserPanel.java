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
import pl.marek.knx.pages.FormsPanel.FormType;

@HtmlFile("projectchooser.html")
public class ProjectChooserPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private DBManager dbManager;

	private Button newProjectButton;
	private Button closeButton;

	private List<Project> projects;

	private static boolean execute;

	public ProjectChooserPanel(String componentName, DBManager dbManager) {
		super(componentName);
		setOutputMarkupId(true);
		execute = true;
		this.dbManager = dbManager;
		projects = new ArrayList<Project>(dbManager.getAllProjects());
		loadComponents();
	}

	private void loadComponents() {
		removeAll();

		RepeatingView projectsView = new RepeatingView("project");
		for (Project project : projects) {
			ProjectItem item = new ProjectItem(projectsView.newChildId(),
					new Model<Project>(project));

			PopupMenu menu = item.getPopupMenu();
			PopupMenuItem editMenu = menu.createPopupMenuItem("Edytuj",
					"images/logo.png", true);
			PopupMenuItem deleteMenu = menu.createPopupMenuItem("Usuń",
					"images/logo.png", true);

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
				String pId = getParameter("project");
				if (pId == null) {
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
				Index index = (Index) getPage();
				FormsPanel forms = index.getFormsPanel();
				forms.setType(FormType.NEW_PROJECT);
				forms.setFormPanel(new CreateEditProjectFormPanel("form", dbManager));
				target.add(forms);
				target.appendJavaScript("initFormDialog('"+ getString("new-project") + "'); showFormDialog();");
			}
		});
		add(newProjectButton);

	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		String pId = getParameter("project");
		if (pId == null) {
			tag.put("style", "display: block;");
		}
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
			
			Index index = (Index) getPage();
			FormsPanel forms = index.getFormsPanel();
			forms.setType(FormType.EDIT_PROJECT);
			forms.setFormPanel(new CreateEditProjectFormPanel("form", dbManager, project));
			target.add(forms);
			target.appendJavaScript("initFormDialog('"+ getString("edit-project") + "'); showFormDialog();");
			
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
			System.out.println("delete");
			execute = false;
		}
	}

}
