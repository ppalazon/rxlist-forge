
public class Componente1 {
    
    public void static Component1()
    {
	Componente1.getMessage("prop1.key1");
	Componente1.getMessage("prop1.key2");
	System.out.println(Componente1.getMessage("prop1.key4"));
	
	System.out.println(Componente1.getMessage("prop1.key4")+Componente1.getMessage("prop1.key3"));
    }
    
    public static String getMessage(String key)
    {
	
    }

}
