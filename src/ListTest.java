public class ListTest {
    public static void main(String[] args) {
        LList<String> list = new LList<String>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        list.add("bb");
        list.add("cc");
        list.add("aa");
        list.add("bb");
        list.add("cc");

        System.out.print(list.toString());



        

        
    }

    
} 