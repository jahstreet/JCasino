package by.sasnouskikh.jcasino.test;

class Quest {

    public static void main(String[] args) {
        Foo foo = new SubFoo();
        foo.bar();
    }

    public static class Foo {
        public void bar() {
            System.out.println("bar inner");
        }
    }

    public static class SubFoo extends Foo {
        public void bar() {
            System.out.println("bar inner inner");
        }
    }
}

