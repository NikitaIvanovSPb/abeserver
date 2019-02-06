//package ru.nikita.abeserver.validators;
//
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class AtrsValidatorUtil {
//
//    public static void main(String[] args) {
//        Pattern p = Pattern.compile("[\\w]+(\\s*=\\s*[\\w]+)?");
//        Matcher matcher = p.matcher("b=");
//    }
//
//    public static List<String> isPermissionValid(String string){
//
//    }
//
//    public static List<String> isRolesValid(String string){
//        if(string.isEmpty()) return false;
//        String[] atrs = string.split("|");
//        Pattern p = Pattern.compile("[\\w]+(\\s*=\\s*[\\w]+)?");
//        for(String atr : atrs){
//            if(!p.matcher(atr).matches()) return false
//        }
//    }
//
//    public static boolean
//}
