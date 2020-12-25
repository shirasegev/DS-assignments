public class HashFunc {
	public int a;
	public int b;

	HashFunc(String s) {
		String[] val = s.split("_");
		a = Integer.parseInt(val[0]);
		b = Integer.parseInt(val[1]);
	}
}