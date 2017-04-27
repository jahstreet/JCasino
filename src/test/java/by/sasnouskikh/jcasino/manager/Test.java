package by.sasnouskikh.jcasino.manager;

public enum Test implements Guru, Muru {
    ALIAS;
    public static void main(String[] args) {
        System.out.println(Guru.text);
    }

    public Object doString() {
        return "";
    }
}

interface Guru{
    String text = "a";
//    default void goHere(){}
//    void doSmth();
}
interface Muru{
    String text = "a";
    default void goHere(){}
//    void doSmth();
}