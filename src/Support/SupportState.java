package Support;

public abstract class SupportState {
    public Support support;

    public SupportState(Support support){
        this.support = support;
    }


    public abstract void doSomeThing();
}
