public class Laternfish {
    private long total;
    private int timer;


    public Laternfish(int timer, long total) {
        this.timer = timer;
        this.total = total;
    }

    public Laternfish cycle() {
        if (timer == 0) {
            timer = 6;
            return new Laternfish(8, total);
        }

        timer--;
        return null;
    }

    public long getTotal() {
        return total;
    }

    public int getTimer() {
        return timer;
    }

    public void incrementTotal() {
        this.total++;
    }

    public void addToTotal(long incoming) {
        this.total = total + incoming;
    }
}
