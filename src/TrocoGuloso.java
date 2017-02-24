class TrocoGuloso{
	
	private static final int[] moedas = new int[]{ 100,50,25,20,10,5,1 };

	public static void troco(int valor){
		int moedas_usadas = 0;
		for (int moeda:moedas) {
			if( valor >= moeda){
				while( valor >= moeda ){
					valor = valor - moeda;
					moedas_usadas += 1;
				}
				System.out.println(moedas_usadas + " moedas de " + moeda);
				moedas_usadas = 0;
			}
		}
	}

	public static void trocoMetodo2(int valor){
		for (int moeda:moedas) {
			if(valor >= moeda)
				System.out.println(valor/moeda + " moedas de " + moeda);
				valor = valor % moeda;
		}
	}


	public static void main(String[] args) {
		TrocoGuloso.trocoMetodo2(90);
	}
}

