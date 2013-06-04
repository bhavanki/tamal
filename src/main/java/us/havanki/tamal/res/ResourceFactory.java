package us.havanki.tamal.res;

/**
 * A factory for Resource objects.
 */
public class ResourceFactory {

    // this is a singleton
    private static final ResourceFactory INSTANCE = new ResourceFactory();
    public static ResourceFactory getInstance() { return INSTANCE; }

    private static final int MAX_RESOURCES = 256;

    private Resource[] resources = new Resource [MAX_RESOURCES];
    private ResourceFactory() {
        // tbd read from config file or something
        resources[0] = Resources.BLUE_FLOWER;
        resources[1] = Resources.RED_FLOWER;
        resources[2] = Resources.PURPLE_FLOWER;

        resources[3] = Resources.ROCK_WARBLER_EGG;
        resources[4] = Resources.PINE_THRUSH_EGG;

        resources[5] = Resources.CREEP_CLUSTER;
    }

    public Resource getResource (int id) {
        return resources[id];
    }

}
