import java.util.Arrays;

public class rank {

    public static String[] reRank(String[] input){

        /*if (input==null)
        {
            return null;
        }*/

        if(input==null||input.length==0){
            return new String[0];
        }

        int r=0,g=0,b=0;
        for (String s:input) {
            if (s.equals("r")){
                r+=1;
            }else if (s.equals("g")){
                g+=1;
            }else if (s.equals("b")){
                b+=1;
            }
        }

        String[] ans = new String[input.length];

        int i=0;
        for (i=0;i<r;i++){
            ans[i]="r";
        }
        for(i=r;i<r+g;i++){
            ans[i]="g";
        }
        for(i=r+g;i<r+g+b;i++){
            ans[i]="b";
        }

        return ans;
    }

    public static String[] reRankTwoPointers(String[] input){
        if(input==null||input.length==0){
            return new String[0];
        }
        int i=0;
        int left=0;
        int right=input.length-1;
        while (i<=right){
            if(input[i].equals("r")){
                String t1 =input[i];
                input[i]=input[left];
                input[left]=t1;
                i++;
                left++;
            }else if (input[i].equals("g")){
                i++;
            }else if (input[i].equals("b")){
                String t2=input[i];
                input[i]=input[right];
                input[right]=t2;
                right--;
            }
        }
        return input;
    }

    public static void main(String[] args) {
        String[] input = new String[]{"r","r","b","g","b","r","g"};
        //Arrays.asList(reRank(input)).forEach(e->System.out.println(e));
        Arrays.asList(reRankTwoPointers(input)).forEach(e->System.out.println(e));
    }
}
