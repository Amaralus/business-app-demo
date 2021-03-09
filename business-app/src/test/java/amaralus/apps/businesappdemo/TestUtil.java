package amaralus.apps.businesappdemo;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.entities.Alpha;
import amaralus.apps.businesappdemo.entities.AlphaVersion;

public final class TestUtil {

    private TestUtil() {
        throw new AssertionError();
    }

    public static Alpha alpha() {
        return alpha("code1");
    }

    public static Alpha alpha(String code) {
        return alpha(code, null, null);
    }

    public static Alpha alpha(String code, String updateField) {
        return alpha(code, updateField, null);
    }

    public static Alpha alpha(String code, AlphaVersion alphaVersion) {
        return alpha(code, null, alphaVersion);
    }

    public static Alpha alpha(String code, String updateField, AlphaVersion alphaVersion) {
        return Alpha.builder()
                .code(code)
                .updateField(updateField)
                .version(alphaVersion)
                .build();
    }

    public static AlphaModel alphaModel() {
        return alphaModel("code1");
    }

    public static AlphaModel alphaModel(String code) {
        return AlphaModel.builder()
                .alphaCode(code)
                .build();
    }

    public static AlphaVersion alphaVersion() {
        return alphaVersion("0.1");
    }

    public static AlphaVersion alphaVersion(String version) {
        return alphaVersion(version, null);
    }

    public static AlphaVersion alphaVersion(String version, String updateField) {
        return AlphaVersion.builder()
                .versionValue(version)
                .updateField(updateField)
                .build();
    }
}
