package engine.math;

import engine.main.Window;

public class Matrix4f
{
	float m[][];

	public Matrix4f()
	{
		m = new float[4][4];
		identity();
	}
	
	public Matrix4f identity()
	{
		m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
		m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = 0;
		m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = 0;
		m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f scale(Vector3f f)
	{
		m[0][0] = f.getX(); m[0][1] = 		0f;	m[0][2] = 		0f; m[0][3] = 0f;
		m[1][0] = 		0f; m[1][1] = f.getY();	m[1][2] = 		0f; m[1][3] = 0f;
		m[2][0] = 		0f; m[2][1] = 		0f;	m[2][2] = f.getZ(); m[2][3] = 0f;
		m[3][0] = 		0f; m[3][1] = 		0f;	m[3][2] = 		0f; m[3][3] = 1f;
		
		return this;
	}

	public Matrix4f translate(Vector3f f)
	{
		m[0][0] = 1f; m[0][1] = 0f;m[0][2] = 0f; m[0][3] = f.getX();
		m[1][0] = 0f; m[1][1] = 1f;m[1][2] = 0f; m[1][3] = f.getY();
		m[2][0] = 0f; m[2][1] = 0f;m[2][2] = 1f; m[2][3] = f.getZ();
		m[3][0] = 0f; m[3][1] = 0f;m[3][2] = 0f; m[3][3] = 1f;
		
		return this;
	}
	
	public Matrix4f rotate(Vector3f f)
	{
		float angleX = (float) Math.toRadians(f.getX());
		float angleY = (float) Math.toRadians(f.getY());
		float angleZ = (float) Math.toRadians(f.getZ());
			
		Matrix4f mX = new Matrix4f();
		Matrix4f mY = new Matrix4f();
		Matrix4f mZ = new Matrix4f();
		
		mX.m[0][0] = 1; mX.m[0][1] = 0; 				 		mX.m[0][2] = 0;								mX.m[0][3] = 0;
		mX.m[1][0] = 0; mX.m[1][1] = (float) Math.cos(angleX); 	mX.m[1][2] = - (float) Math.sin(angleX); 	mX.m[1][3] = 0;
		mX.m[2][0] = 0; mX.m[2][1] = (float) Math.sin(angleX);  mX.m[2][2] = (float) Math.cos(angleX); 		mX.m[2][3] = 0;
		mX.m[3][0] = 0; mX.m[3][1] = 0; 				  		mX.m[3][2] = 0; 							mX.m[3][3] = 1;

		
		mY.m[0][0] = (float) Math.cos(angleY);		mY.m[0][1] = 0;	mY.m[0][2] = (float) -Math.sin(angleY);	mY.m[0][3] = 0;
		mY.m[1][0] = 0;								mY.m[1][1] = 1;	mY.m[1][2] = 0; 						mY.m[1][3] = 0;
		mY.m[2][0] = (float) Math.sin(angleY); 		mY.m[2][1] = 0; mY.m[2][2] = (float) Math.cos(angleY);	mY.m[2][3] = 0;
		mY.m[3][0] = 0; 							mY.m[3][1] = 0;	mY.m[3][2] = 0; 						mY.m[3][3] = 1;
		
		mZ.m[0][0] = (float) Math.cos(angleZ);	mZ.m[0][1] = (float) -Math.sin(angleZ); 	mZ.m[0][2] = 0; mZ.m[0][3] = 0;
		mZ.m[1][0] = (float) Math.sin(angleZ);	mZ.m[1][1] = (float) Math.cos(angleZ); 		mZ.m[1][2] = 0; mZ.m[1][3] = 0;
		mZ.m[2][0] = 0;							mZ.m[2][1] = 0;						 		mZ.m[2][2] = 1; mZ.m[1][3] = 0;
		mZ.m[3][0] = 0; 						mZ.m[3][1] = 0;								mZ.m[3][2] = 0; mZ.m[3][3] = 1;		
		
		m = mZ.mul(mY.mul(mX)).getM();
				
		mX = null;
		mZ = null;
		mY = null;

		return this;
	}
	
	/**
	 * Credit to the Wikipedia article on rotation around an arbitrary axis.
	 */
	
	public Matrix4f arbitraryAxisRotate(Vector3f axis, float angle)
	{
		float theta = (float) Math.toRadians(angle);
		
		float c = (float) Math.cos(theta);
		float s = (float) Math.sin(theta);
		float t = (1f - c);
		
		float x_ = axis.getX();
		float y_ = axis.getY();
		float z_ = axis.getZ();
				
		m[0][0] = (t * x_ * x_) + c;			m[0][1] = (t * x_ * y_) - (s * z_); 	m[0][2] = (t * z_ * x_) + (s * y_); 	m[0][3] = 0;
		m[1][0] = (t * y_ * x_) + (s * z_); 	m[1][1] = (t * y_ * y_) + c; 			m[1][2] = (t * y_ * z_) - (s * x_); 	m[1][3] = 0;
		m[2][0] = (t * z_ * x_) - (s * y_); 	m[2][1] = (t * y_ * z_) + (s * x_); 	m[2][2] = (t * z_ * z_) + c; 			m[2][3] = 0;
		m[3][0] = 0; 							m[3][1] = 0;						 	m[3][2] = 0; 							m[3][3] = 1;
		
		return this;			
	}
	
	public Matrix4f perspective(int fov, float zNear, float zFar)
	{
		float tanHalfFOV = (float)Math.tan(Math.toRadians((fov / 2)));
		float ar = Window.ASPECT_RATIO;
		float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * ar); 	m[0][1] = 0; 					m[0][2] = 0; 							m[0][3] = 0;
		m[1][0] = 0;			  				m[1][1] = 1.0f / (tanHalfFOV); 	m[1][2] = 0; 							m[1][3] = 0;
		m[2][0] = 0; 			  				m[2][1] = 0; 					m[2][2] = (- zNear - zFar) / zRange; 	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0; 							m[3][1] = 0; 					m[3][2] = 1;							m[3][3] = 0;
		
		return this;
	}
	
	public Matrix4f initCamera(Vector3f forward, Vector3f up)
	{
		Vector3f f = forward.normalized();

		Vector3f r = up.normalized();
		r = r.cross(f);

		Vector3f u = f.cross(r);
		
		m[0][0] = r.getX(); m[0][1] = r.getY(); m[0][2] = r.getZ(); m[0][3] = 0;
		m[1][0] = u.getX();	m[1][1] = u.getY(); m[1][2] = u.getZ();	m[1][3] = 0;
		m[2][0] = f.getX(); m[2][1] = f.getY(); m[2][2] = f.getZ(); m[2][3] = 0;
		m[3][0] = 0; 		m[3][1] = 0; 		m[3][2] = 0; 		m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f mul(Matrix4f r)
	{
		Matrix4f product = new Matrix4f();
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				float f = m[i][0] * r.get(0, j) +
						  m[i][1] * r.get(1, j) +
						  m[i][2] * r.get(2, j) +
						  m[i][3] * r.get(3, j);
						
				product.set(i, j, f);
			}
		}
		
		return product;
		
	}
	
	public Vector3f mul(Vector3f f)
	{
		float x = f.getX();
		float y = f.getY();
		float z = f.getZ();
		float w = 1.0f;
		
		return new Vector3f(
				x*m[0][0] + y*m[0][1] + z*m[0][2] + w*m[0][3],	
				x*m[1][0] + y*m[1][1] + z*m[1][2] + w*m[1][3],
				x*m[2][0] + y*m[2][1] + z*m[2][2] + w*m[2][3]			
				);
	}
	
	public float get(int i, int j)
	{
		return m[i][j];
	}
	
	public void set(int i, int j, float f)
	{
		m[i][j] = f;
	}
	
	public float[][] getM()
	{
		return m;
	}
	
	public void setM(float[][] m)
	{
		this.m = m;
	}
	
	public String toString()
	{
		return new String(	"Matrx 4x4 : \n" +
							m[0][0] + " " + m[0][1] + " " + m[0][2] + " " + m[0][3] + " \n" + 
							m[1][0] + " " + m[1][1] + " " + m[1][2] + " " + m[1][3] + " \n" + 
							m[2][0] + " " + m[2][1] + " " + m[2][2] + " " + m[2][3] + " \n" + 
							m[3][0] + " " + m[0][1] + " " + m[3][2] + " " + m[3][3]
						);
	}
}
