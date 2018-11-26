package ViewFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Progress {

	public int n;					//进程数量
	public int m;					//资源种类
	public int[][] Max;				//最大需求矩阵
	public int[][] Allocation;		//分配矩阵
	public int[][] Need;			//需求矩阵
	public int[] Available;			//可利用资源向量
	public int[] Resource;			//资源初始值
	public int[] Request;			//进程请求向量
	public int[] Work;				//工作向量
	public boolean[] Finish;		//是否有足够资源分配给进程
	public int[] safeSeq;			//安全序列
	public boolean FLAG;			//安全状态标志
	public List<String> list = new ArrayList<String>();
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * 用于测试当前状态
	 * 打印Max  Allocation  Need  Available
	 */
	
	public void currentState() {
		for (int i=0;i<n;i++) 
			for (int j=0;j<m;j++) 
				Need[i][j] = Max[i][j] - Allocation[i][j];
		
		System.out.println("\tMax\t\tAllocation\t\tNeed\t\tAvailable");
		for (int i=0;i<4;i++) {
			System.out.print("\t");
			for (int j=0;j<m;j++) 
				System.out.print("资源"+j+" ");
		}
		System.out.println();
		for (int i=0;i<n;i++) {
			System.out.print("进程"+i+"    ");
			for (int j=0;j<m;j++) 
				System.out.print(Max[i][j]+" ");
			System.out.print("\t\t");
			for (int j=0;j<m;j++) 
				System.out.print(Allocation[i][j]+" ");
			System.out.print("\t\t");
			for (int j=0;j<m;j++) 
				System.out.print(Need[i][j]+" ");
			if (i == 0) {
				System.out.print("\t\t");
				for (int j=0;j<m;j++) 
					System.out.print(Available[j]+" ");
			}
			System.out.println();
		}
	}
		
	/**
	 * 检查进程i是否满足Need[i,j]<=Work[j]
	 * @param   进程i
	 * @return  若存在返回true，否则返回false
	 */
	public boolean check(int i) {
		for (int j=0;j<m;j++) 
			if (Need[i][j] > Work[j])
				return false;
		return true;
	}
	
	/**
	 * 查找当前状态是否存在安全序列
	 * @param step	进程step
	 */
	public void search(int step) {
		if (step == n) {
			FLAG = true;
			return;
		}
		for (int i=0;i<n;i++) {
			if (!Finish[i]) {
				if (check(i)) {
					for (int j=0;j<m;j++) {
						Work[j] += Allocation[i][j];
						Finish[i] = true;
						safeSeq[step] = i;
					}
					list.add("进程"+i+"请求分配资源，获得分配资源");
					search(step+1);
				}else {
					list.add("进程"+i+"请求分配资源，无法获得资源");
				}
			}else 
				if (step <= n-1)
					list.add("进程"+i+"已分配资源，无需分配资源");
		}
		System.out.println("search()执行了一次"+list);
	}
	
	/**
	 * 进程x请求资源
	 * @param x  进程x
	 * @param RequestPa  请求资源向量
	 * @return	若请求成功，则返回success，否则返回"系统不分配资源"或"进程等待"
	 */
	public String request(int x, int[] RequestPa) {
		list.clear();
		for (int k=0;k<m;k++) 
			Work[k] = Available[k];
		for (int i=0;i<n;i++) {
			Finish[i] = false;
			safeSeq[i] = 0;
		}
		for (int j=0;j<m;j++) 
			Request[j] = RequestPa[j];
		for (int j=0;j<m;j++) {
			if (Request[j] > Need[x][j]) 
				return "系统不分配资源";
			else 
				if (Request[j] > Available[j]) 
					return "进程等待";
		}
		for (int k=0;k<m;k++) {
			Need[x][k] -= Request[k];
			Available[k] -= Request[k];
			Allocation[x][k] += Request[k];
		}
		for (int k=0;k<m;k++) 
			Work[k] = Available[k];
		System.out.println("request()  currentState");
		currentState();
		search(0);
//		for (int k=0;k<m;k++) {
//			Need[x][k] += Request[k];
//			Available[k] += Request[k];
//			Allocation[x][k] -= Request[k];
//		}
		if (FLAG == false)
			return "安全检查不符合";
		
		return "success";
	}
	

	/**
	 * 输入最大需求矩阵Max
	 * @param MaxPa
	 */
	public void inputMax(int[][] MaxPa) {
		for (int i=0;i<n;i++) 
			for (int j=0;j<m;j++)
				Max[i][j] = MaxPa[i][j];
	}
	
	/**
	 * 输入分配矩阵Allocation
	 * @param AllocationPa
	 */
	public void inputAllocation(int[][] AllocationPa) {
		for (int i=0;i<n;i++) 
			for (int j=0;j<m;j++)
				Allocation[i][j] = AllocationPa[i][j];
	}
	
	/**
	 * 输入可利用资源向量Available
	 * @param AvailablePa
	 */
	public void inputResource(int[] ResourcePa) {
		for (int k=0;k<m;k++) {
			int sum = 0;
			for (int i=0;i<n;i++) 
				sum += Allocation[i][k];
			Available[k] = ResourcePa[k] - sum;
			Work[k] = Available[k];
		}
	}
	
	/**
	 * 判断初始状态是否安全
	 */
	public void isSafe() {
		for (int k=0;k<m;k++) 
			Work[k] = Available[k];
		search(0);
	}
	
	/**
	 * 矩阵向量Max，Allocation，Need，Available全部输入完整后开始执行start()
	 */
	public void start() {
		System.out.println(n+" "+m);
		Max = new int[n][m];
		Allocation = new int[n][m];
		Need = new int[n][m];
		Available = new int[m];
		Resource = new int[m];
		Request = new int[m];
		Work = new int[m];
		Finish = new boolean[n];
		safeSeq = new int[10];
		currentState();
	}
}