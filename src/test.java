/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
class b{
    int a= 9;
    public void show(){
        System.out.println(this.a);
    }
}

class bO extends b{
    int a;
    bO(int a){
        this.a = a;
    }
    public void show(){
        System.out.println(super.a);
    }
}
/**
 *
 * @author abhia
 */
public class test {
    public static void main(String... args){
    b bb = new bO(10);
    bb.show();
    }
}
