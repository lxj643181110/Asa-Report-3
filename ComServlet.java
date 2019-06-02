package com.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import sun.text.normalizer.IntTrie;

import com.bean.ComBean;
import com.bean.ExpressageBean;
import com.util.Constant;
import com.util.SmartFile;
import com.util.SmartUpload;
import com.util.SmartUploadException;
/**
 * 
 * 处理业务 ，添加快递信息等操作都在这里进行
 * 
 * @author Ray
 *
 */
public class ComServlet extends HttpServlet {

	private ServletConfig config;
	public ComServlet() {
		super();
	}

	final public void init(ServletConfig config) throws ServletException
    {
        this.config = config;  
    }

    final public ServletConfig getServletConfig()
    {
        return config;
    }
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType(Constant.CONTENTTYPE);
		request.setCharacterEncoding(Constant.CHARACTERENCODING);
		HttpSession session = request.getSession();
		ComBean cBean = new ComBean();
		String date2=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
		String method = request.getParameter("method");
		
		
		if(method.equals("addTH")){  //add 增加
			String title=request.getParameter("title");
			String content=request.getParameter("infoContent");   
				 int flag = cBean.comUp("insert into news(title, content,sj ) " +
							"values('"+title+"', '"+content+"', '"+date2+"' )");
					if(flag == Constant.SUCCESS){ 
						request.setAttribute("message", "Successful operation！");
						request.getRequestDispatcher("admin/news/index.jsp").forward(request, response); 
					}
					else { 
						request.setAttribute("message", "operation failed！");
						request.getRequestDispatcher("admin/news/index.jsp").forward(request, response); 
					}
			 
		}
		else if(method.equals("upTH")){ ///update 更新
			String id = request.getParameter("id");
			String title=request.getParameter("title");
			String content=request.getParameter("infoContent");  
			int flag = cBean.comUp("update news set title='"+title+"', content='"+content+"'  where id='"+id+"'");
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("admin/news/index.jsp").forward(request, response); 
			}
			else if(flag == Constant.NAME_ERROR){ 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("admin/news/index.jsp").forward(request, response); 
			}
		}
		else if(method.equals("delTH")){  //del teacher
			String id = request.getParameter("id");
			int flag = cBean.comUp("delete from news where id='"+id+"'");
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("admin/news/index.jsp").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("admin/news/index.jsp").forward(request, response); 
			}
		}
		else if(method.equals("qyJS")){  //del teacher
			String js = request.getParameter("infoContent");
			int flag = cBean.comUp("update js set js='"+js+"'");
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("admin/qy/index.jsp").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("admin/qy/index.jsp").forward(request, response); 
			}
		}
		
		else if(method.equals("delQC")){  //del teacher
			String id = request.getParameter("id");
			int flag = cBean.comUp("delete from qc where id='"+id+"'");
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("admin/hzp/index.jsp").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("admin/hzp/index.jsp").forward(request, response); 
			}
		}
		
		
		else if(method.equals("addPREP")){  
			//添加一条快递信息
			 SmartUpload mySmartUpload = new SmartUpload();//init
			 String path = "";
			 SmartFile file= null;
			 mySmartUpload.initialize(config,request,response);
	         try {
				mySmartUpload.upload();
				file = mySmartUpload.getFiles().getFile(0);
		    	String fileExt=file.getFileExt();	            
		    	path="/upload_file/sale";
		        int count = mySmartUpload.save(path);
	         } catch (SmartUploadException e) {
	 			e.printStackTrace();
	 		} 
	         
	         
	        path=path+"/"+file.getFileName();
	        
			int  userid = Integer.parseInt(request.getParameter("id"));
			String type=mySmartUpload.getRequest().getParameter("type");
			String username=mySmartUpload.getRequest().getParameter("username");;
			String address =mySmartUpload.getRequest().getParameter("address");
			String raddress =mySmartUpload.getRequest().getParameter("raddress");
			String begintime=mySmartUpload.getRequest().getParameter("begintime");
		
			float money=Float.parseFloat(mySmartUpload.getRequest().getParameter("money"));
			
			String phone =mySmartUpload.getRequest().getParameter("phone");
			
			
			String member=(String)session.getAttribute("member");

			//插入新的一条快递信息
			ExpressageBean ex=new ExpressageBean();
			int flag=ex.addExpressage(username, type, address, raddress, path, phone, begintime, money, userid);
            
			
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("./expressage").forward(request, response);
			
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("nhzp.jsp").forward(request, response); 
			}
		}
		else if(method.equals("upPREP")){ 
			
			int  id = Integer.parseInt(request.getParameter("id"));
			String username = request.getParameter("username");
			String phone = request.getParameter("phone");
			String type = request.getParameter("type");
			String money = request.getParameter("money"); 
			String begintime=request.getParameter("begintime");
			String address=request.getParameter("address");
			String raddress=request.getParameter("raddress");
			
			ExpressageBean ex=new ExpressageBean();
			int flag=ex.updateExpressageByid(id,username,phone,type,money,begintime,address,raddress);
			
			

			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
//				request.getRequestDispatcher("member/prep/index.jsp").forward(request, response); 
				request.getRequestDispatcher("./ExpressageManager").forward(request, response); 
				return ;		        
			}
			else { 
				request.setAttribute("message", "operation failed! Can not modify the delivery information after confirmation");
				request.getRequestDispatcher("./ExpressageManager").forward(request, response); 
				return ;
			}
		}
else if(method.equals("upPREP2")){ 
			
			int  id = Integer.parseInt(request.getParameter("id"));
			String username = request.getParameter("username");
			String phone = request.getParameter("phone");
			String type = request.getParameter("type");
			String money = request.getParameter("money"); 
			String begintime=request.getParameter("begintime");
			String address=request.getParameter("address");
			String raddress=request.getParameter("raddress");
			
			ExpressageBean ex=new ExpressageBean();
			int flag=ex.updateExpressageByid(id,username,phone,type,money,begintime,address,raddress);
			
			

			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("./ExpressageManager?action=all2").forward(request, response); 
				return ;		        
			}
			else { 
				request.setAttribute("message", "operation failed! Can not modify the delivery information after confirmation");
				request.getRequestDispatcher("./ExpressageManager?action=all2").forward(request, response); 
				return ;
			}
		}
		
		/*
		else if(method.equals("backPREP"))	//
		{
			String id = request.getParameter("id");
			System.out.println("-----------------"+id);
			String sql="update qc set useable='未租出' where id = (select qcid from zc where id = '"+id+"')";
			String sql2 = "update zc set  zt = '已还车' where id = '"+id+"'";
			int flag = cBean.comUp(sql);
			int flag2 = cBean.comUp(sql2);
			if(flag == Constant.SUCCESS&&flag2 == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("member/prep/index.jsp").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("member/prep/index.jsp").forward(request, response); 
			}
		}*/
		else if(method.equals("delP")){  //del teacher
			int  id = Integer.parseInt(request.getParameter("id"));
		  //根据id将对应字段的isdeleted设置为true
		    ExpressageBean ex=new ExpressageBean();
		    int flag=  ex.becollect(id);
				
			
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("./ExpressageManager").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("member/prep/index.jsp").forward(request, response); 
			}
		}
		else if(method.equals("delete")){  //del teacher
			int  id = Integer.parseInt(request.getParameter("id"));
		    ExpressageBean ex=new ExpressageBean();
		    int flag=  ex.deleteOne2(id);	
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("./ExpressageManager").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("member/prep/index.jsp").forward(request, response); 
			}
		}
		else if(method.equals("delP2")){  //del teacher
			int  id = Integer.parseInt(request.getParameter("id"));
		  //根据id将对应字段的isdeleted设置为true
		    ExpressageBean ex=new ExpressageBean();
		    int flag=  ex.becollect(id);
				
			
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("./ExpressageManager?action=all2").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("./ExpressageManager?action=all2").forward(request, response); 
			}
		}
		else if(method.equals("delete2")){  //del teacher
			int  id = Integer.parseInt(request.getParameter("id"));
		    ExpressageBean ex=new ExpressageBean();
		    int flag=  ex.deleteOne2(id);	
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("./ExpressageManager?action=all").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("./ExpressageManager?action=all").forward(request, response); 
			}
		}
		
		
		
		else if(method.equals("AdelP")){  //del teacher
			String id = request.getParameter("id");
			int flag = cBean.comUp("delete from zc where id='"+id+"'");
			if(flag == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("admin/prep/index.jsp").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("admin/prep/index.jsp").forward(request, response); 
			}
		}
		else if(method.equals("shP")){  //del 
			String id = request.getParameter("id");
			int flag = cBean.comUp("update zc set zt='Audited' where id='"+id+"'");
			int flag2 = cBean.comUp("update qc set useable='Leased out' where id = (select qcid from zc where id = '"+id+"')");
			if(flag == Constant.SUCCESS&&flag2 == Constant.SUCCESS){ 
				request.setAttribute("message", "Successful operation！");
				request.getRequestDispatcher("admin/prep/index.jsp").forward(request, response); 
			}
			else { 
				request.setAttribute("message", "operation failed！");
				request.getRequestDispatcher("admin/prep/index.jsp").forward(request, response); 
			}
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
