package ying.jie.entity;

public abstract class EventBusBase {

    protected int count;

    public abstract int getCount();

    public void setCount(int count) {
        this.count = count;
    }

    public static EventBusBase getInstance(final int type) {
        EventBusBase eventBusBase = null;
        switch (type) {
            case 0:
                eventBusBase = new EventBusTime();
                break;
            case 1:
                eventBusBase = new EventBusTimeTmp();
                break;
            default:
                break;
        }
        return eventBusBase;
    }

}
