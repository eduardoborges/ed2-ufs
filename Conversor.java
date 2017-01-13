import java.nio.ByteBuffer;

class Conversor {

	public static ByteBuffer getBuffer(Aluno a){

		ByteBuffer buf = ByteBuffer.allocate(200);

		buf.putLong( a.getMatric() );
		buf.position(68);
		buf.put( a.getNome().getBytes() );
		buf.position(148);
		buf.put( a.getEndereco().getBytes() );
		buf.position();
		buf.put( a.getEmail().getBytes() );
		buf.position(198);
		buf.putShort( a.getCurso() );
		buf.position(0);

		return buf;
	}

	public static Aluno getAluno(ByteBuffer buf){
		Aluno a = new Aluno();

		a.setMatric(buf.getLong());

		byte[] vNome = new byte[60];
		buf.get(vNome);
		String nome = new String(vNome);
		nome = nome.trim();
		a.setNome(nome);

		byte[] vEndereco = new byte[80];
		buf.get(vEndereco);
		String endereco = new String(vEndereco);
		endereco = endereco.trim();
		a.setNome(endereco);

		byte[] vEmail = new byte[50];
		buf.get(vEmail);
		String email = new String(vEmail);
		email = email.trim();
		a.setNome(email);

		a.setCurso(buf.getShort());

		return a;

	}

	public static void main(String[] args) {
		Aluno aluno = new Aluno(6546547, "Eduardo", "Foda-se", "eduardo@ufs.br", (short)8);
		ByteBuffer buffer =	getBuffer(aluno);


	}
}