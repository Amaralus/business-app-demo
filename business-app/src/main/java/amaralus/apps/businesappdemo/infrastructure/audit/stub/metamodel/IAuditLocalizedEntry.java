package amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel;

import java.util.Map;

public interface IAuditLocalizedEntry {

    Map<Lang, String> getLocalization();

    String getCode();

    enum Lang {RUS, END}
}
