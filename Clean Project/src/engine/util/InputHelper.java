package engine.util;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

public  class InputHelper
{
	private static HashMap<Integer, Boolean> keysUp;
	private static HashMap<Integer, Boolean> keysDown;
	private static HashMap<Integer, Boolean> keysPressed;
	private static HashMap<Integer, Boolean> keysReleased;
	
	public InputHelper()
	{
		keysUp = new HashMap<Integer, Boolean>();
		keysDown = new HashMap<Integer, Boolean>();
		keysPressed = new HashMap<Integer, Boolean>();
		keysReleased = new HashMap<Integer, Boolean>();
		
		for(int i = 0x01; i <= 0xDB; i++)
		{
			keysUp.put(i, true);
		}
		
		for(int i = 0x01; i <= 0xDB; i++)
		{
			keysDown.put(i, false);
		}
		
		for(int i = 0x01; i <= 0xDB; i++)
		{
			keysReleased.put(i, false);
		}
		

		for(int i = 0x01; i <= 0xDB; i++)
		{
			keysPressed.put(i, false);
		}
	}
	
	public static void update()
	{
		for(int i = 0x01; i <= 0xDB; i++)
		{
			if(Keyboard.isKeyDown(i) && !keysDown.get(i))
			{
				keysPressed.put(i, true);
			}
			else
			{
				keysPressed.put(i, false);
			}
			
			if(!Keyboard.isKeyDown(i) && keysDown.get(i))
			{
				keysReleased.put(i, true);
			}
			else
			{
				keysReleased.put(i, false);
			}
			
			if(Keyboard.isKeyDown(i))
			{
				keysDown.put(i, true);
			}
			else
			{
				keysDown.put(i,  false);
			}
			
			if(!Keyboard.isKeyDown(i))
			{
				keysUp.put(i, true);
			}
			else
			{
				keysUp.put(i,  false);
			}		
		}
	}
	
	public static boolean isKeyDown(int key)
	{
		return keysDown.get(key);
	}
	
	public static boolean isKeyUp(int key)
	{
		return keysUp.get(key);
	}
	
	public static boolean isKeyReleased(int key)
	{
		return keysReleased.get(key);
	}
	
	public static boolean isKeyPressed(int key)
	{
		return keysPressed.get(key);
	}

}
