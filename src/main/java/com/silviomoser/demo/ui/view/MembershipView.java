package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Membership;
import com.silviomoser.demo.data.Organization;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.repository.MembershipRepository;
import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.ui.editor.MembershipEditor;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.silviomoser.demo.utils.FormatUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.LocalDateRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static com.silviomoser.demo.utils.FormatUtils.toFirstLastName;

/**
 * Created by silvio on 10.10.18.
 */

@Component
@SpringView(name = MembershipView.VIEW_NAME)
public class MembershipView extends VerticalLayout implements View {

    @Autowired
    MembershipEditor membershipEditor;

    public static final String VIEW_NAME = "membership";

    I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    DateField joinDateEditor = new DateField(i18Helper.getMessage("membership_join"));
    DateField leaveDateEditor = new DateField(i18Helper.getMessage("membership_leave"));
    TextField functionEditor = new TextField("Funktion");
    TextField remarksEditor = new TextField("Bemerkungen");


    TabSheet tabSheet = new TabSheet();


    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private JpaRepository<Organization, Long> organizationRepository;


    @PostConstruct
    public void init() {
        this.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        final Collection<Organization> allOrganizations = organizationRepository.findAll();

        final VerticalLayout mainLayout = new VerticalLayout(tabSheet);

        mainLayout.setWidth(100, Unit.PERCENTAGE);
        mainLayout.setHeight(100, Unit.PERCENTAGE);

        addComponent(NavigationBar.buildNavigationBar(this));
        addComponent(mainLayout);


        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        for (Organization organization : allOrganizations) {
            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);

            final Grid<Membership> grid = new Grid<>();

            grid.setHeight(800, Unit.PIXELS);
            grid.setWidth(100, Unit.PERCENTAGE);

            List<Membership> groupMembers = membershipRepository.findByOrganization(organization);

            grid.setItems(groupMembers);

            Binder<Membership> binder = grid.getEditor().getBinder();
            Binder.Binding<Membership, LocalDate> joinDateTimeBinding = binder.bind(joinDateEditor, Membership::getJoinDate, Membership::setJoinDate);
            Binder.Binding<Membership, LocalDate> leaveDateTimeBinding = binder.bind(leaveDateEditor, Membership::getLeaveDate, Membership::setLeaveDate);
            Binder.Binding<Membership, String> functionBinding = binder.bind(functionEditor, Membership::getFunction, Membership::setFunction);
            Binder.Binding<Membership, String> remarksBinding = binder.bind(remarksEditor, Membership::getRemarks, Membership::setRemarks);

            grid.addColumn(it -> toFirstLastName(it.getPerson())).setCaption(i18Helper.getMessage("membership_header_person"));
            grid.addColumn(it -> it.getJoinDate()).setEditorBinding(joinDateTimeBinding)
                    .setCaption(i18Helper.getMessage("membership_header_joined"))
                    .setRenderer(new LocalDateRenderer(DateTimeFormatter
                            .ofLocalizedDate(FormatStyle.LONG)
                            .withLocale(Locale.GERMAN)));
            grid.addColumn(it -> it.getLeaveDate()).setEditorBinding(leaveDateTimeBinding)
                    .setCaption(i18Helper.getMessage("membership_header_left"))
                    .setRenderer(new LocalDateRenderer(DateTimeFormatter
                            .ofLocalizedDate(FormatStyle.LONG)
                            .withLocale(Locale.GERMAN)));

            grid.addColumn(it -> it.getFunction()).setEditorBinding(functionBinding)
                    .setCaption("Funktion");

            grid.addColumn(it -> it.getRemarks()).setEditorBinding(remarksBinding)
                    .setCaption("Bemerkungen");

            grid.getEditor().setEnabled(true);
            grid.getEditor().setBuffered(false);


            final HorizontalLayout groupActions = new HorizontalLayout();
            final TextField filter = new TextField();

            filter.addValueChangeListener(event -> {
                ListDataProvider<Membership> dataProvider = (ListDataProvider<Membership>) grid.getDataProvider();
                dataProvider.setFilter(Membership::getPerson, s -> personNameContains(s, event.getValue()));
            });

            final Button addNewBtn = new Button("Neuer Eintrag", VaadinIcons.PLUS);
            final Button saveChanges = new Button("Änderungen speichern", VaadinIcons.DISC);

            groupActions.addComponents(filter, addNewBtn, saveChanges);

            layout.addComponents(groupActions, grid);




            tabSheet.addTab(layout, organization.getName());

            saveChanges.addClickListener(e -> {
                membershipRepository.saveAll(groupMembers);
                refresh(groupMembers, organization);
                grid.setItems(groupMembers);
            });

            addNewBtn.addClickListener(item -> {
                //Check, if it is a double-click event
                getUI().addWindow(membershipEditor);

                Membership newMembership = Membership.builder()
                        .joinDate(LocalDate.now())
                        .organization(organization).build();

                membershipEditor.editItem(newMembership);
                //window.setVisible(true);

                //add a listener, which will be executed when the window will be closed
                membershipEditor.addCloseListener(closeEvent -> {
                    getUI().removeWindow(membershipEditor);
                    grid.setItems(membershipRepository.findByOrganization(organization));
                });
            });

        }


    }

    private void refresh(List<Membership> groupMembers, Organization organization) {
        groupMembers = membershipRepository.findByOrganization(organization);
    }

    private boolean personNameContains(Person s, String value) {
        return FormatUtils.toFirstLastName(s).contains(value);
    }
}
