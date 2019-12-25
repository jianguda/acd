class Main {
    int fn() {
        int a;
        int b;
        a = 1;
        b = 2;
        return a + b;
    }

    void main() {
        float f;
        if (fn() > 0) {
            f = 1.0;
        } else {
            f = -1.0;
        }
    }
}
