package ViewFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Progress {

	public int n;					//��������
	public int m;					//��Դ����
	public int[][] Max;				//����������
	public int[][] Allocation;		//�������
	public int[][] Need;			//�������
	public int[] Available;			//��������Դ����
	public int[] Resource;			//��Դ��ʼֵ
	public int[] Request;			//������������
	public int[] Work;				//��������
	public boolean[] Finish;		//�Ƿ����㹻��Դ���������
	public int[] safeSeq;			//��ȫ����
	public boolean FLAG;			//��ȫ״̬��־
	public List<String> list = new ArrayList<String>();
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * ���ڲ��Ե�ǰ״̬
	 * ��ӡMax  Allocation  Need  Available
	 */
	
	public void currentState() {
		for (int i=0;i<n;i++) 
			for (int j=0;j<m;j++) 
				Need[i][j] = Max[i][j] - Allocation[i][j];
		
		System.out.println("\tMax\t\tAllocation\t\tNeed\t\tAvailable");
		for (int i=0;i<4;i++) {
			System.out.print("\t");
			for (int j=0;j<m;j++) 
				System.out.print("��Դ"+j+" ");
		}
		System.out.println();
		for (int i=0;i<n;i++) {
			System.out.print("����"+i+"    ");
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
	 * ������i�Ƿ�����Need[i,j]<=Work[j]
	 * @param   ����i
	 * @return  �����ڷ���true�����򷵻�false
	 */
	public boolean check(int i) {
		for (int j=0;j<m;j++) 
			if (Need[i][j] > Work[j])
				return false;
		return true;
	}
	
	/**
	 * ���ҵ�ǰ״̬�Ƿ���ڰ�ȫ����
	 * @param step	����step
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
					list.add("����"+i+"���������Դ����÷�����Դ");
					search(step+1);
				}else {
					list.add("����"+i+"���������Դ���޷������Դ");
				}
			}else 
				if (step <= n-1)
					list.add("����"+i+"�ѷ�����Դ�����������Դ");
		}
		System.out.println("search()ִ����һ��"+list);
	}
	
	/**
	 * ����x������Դ
	 * @param x  ����x
	 * @param RequestPa  ������Դ����
	 * @return	������ɹ����򷵻�success�����򷵻�"ϵͳ��������Դ"��"���̵ȴ�"
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
				return "ϵͳ��������Դ";
			else 
				if (Request[j] > Available[j]) 
					return "���̵ȴ�";
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
			return "��ȫ��鲻����";
		
		return "success";
	}
	

	/**
	 * ��������������Max
	 * @param MaxPa
	 */
	public void inputMax(int[][] MaxPa) {
		for (int i=0;i<n;i++) 
			for (int j=0;j<m;j++)
				Max[i][j] = MaxPa[i][j];
	}
	
	/**
	 * ����������Allocation
	 * @param AllocationPa
	 */
	public void inputAllocation(int[][] AllocationPa) {
		for (int i=0;i<n;i++) 
			for (int j=0;j<m;j++)
				Allocation[i][j] = AllocationPa[i][j];
	}
	
	/**
	 * �����������Դ����Available
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
	 * �жϳ�ʼ״̬�Ƿ�ȫ
	 */
	public void isSafe() {
		for (int k=0;k<m;k++) 
			Work[k] = Available[k];
		search(0);
	}
	
	/**
	 * ��������Max��Allocation��Need��Availableȫ������������ʼִ��start()
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