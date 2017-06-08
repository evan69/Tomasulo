import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import backend.*;
import backend.FloatPointUnit.OP;

public class Main extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//-----------------------------------------ָ�����------------------------------------------	
	/*	
	 * ָ��������
	 */
	private int cmdn;
	private JPanel panel_instr,instp;
	private JScrollPane scroll_panel_instr;
	private FloatPointUnit fpu;
	/*
	 * ָ�����ݡ�����������
	 */
	private String inst[] = { "", "ST", "LD", "ADDD", "SUBD", "MULD", "DIVD" },
			fx[] = { "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8",
					"F9", "F10",},
			ix[],
			nullx[] = {""};

	/*
	 * ָ��ѡ���
	 */
	private JComboBox instbox[];
	private JLabel instl;
	
	//-----------------------------------------���ܰ�ť------------------------------------------	
	
	/*
	 * ִ��һ�������ã���ʼ
	 */
	private JButton stepbut, resetbut, startbut, lookupbut, savebut, stepnbut, regsavebut, filebut;
	private JPanel panel_but;
	private JTextField memaddr, command_numbers; 
	private JFileChooser fileChooser;
	private JLabel cmdl;
	private int Tn, mempos;
	private ArrayList<Instruction> allinst;
	
	//-----------------------------------------����״̬------------------------------------------	
	private JPanel panel_cdt, cdtp;
	private JScrollPane scroll_panel_cdt;
	private JLabel cdtl;
	private JLabel inst_cdtl[][];
	private String inst_cdtst[][];
			
	//-----------------------------------------LOAD STORE------------------------------------------
	private JPanel panel_load, loadp, panel_store, storep;
	private JLabel loadl, storel;
	private JLabel inst_loadl[][], inst_storel[][];
	private String inst_loadst[][], inst_storest[][];

	//-----------------------------------------Reservation------------------------------------------
	private JPanel panel_rsv, rsvp;
	private JLabel rsvl;
	private JLabel inst_rsvl[][];
	private String inst_rsvst[][];
	
	//-----------------------------------------Register------------------------------------------
	private JPanel panel_reg, regp;
	private JLabel regl;
	private JLabel inst_regl[][];
	private JTextField inst_regt[][];
	private String inst_regst[][];
	
	//-----------------------------------------Memory------------------------------------------
	private JPanel panel_mem, memp;
	private JLabel meml;
	private JLabel inst_meml[][];
	private JTextField inst_memt[][];
	private String inst_memst[][];
	
	void window_init(){
		int label_w = 100;
		//-----------------------------------------ָ�����------------------------------------------	

		int inst_w = 360, inst_h = 240;
		instp = new JPanel(new FlowLayout());
		instp.setPreferredSize(new Dimension(inst_w, inst_h));
		getContentPane().add(instp);
		instl = new JLabel("ָ�����");
		instl.setPreferredSize(new Dimension(label_w, 30));
		instp.add(instl);
		panel_instr = new JPanel(new GridLayout(cmdn, 4));
		panel_instr.setPreferredSize(new Dimension(inst_w - 20, (int)(((float)inst_h * (float)cmdn / 8.0))));
		scroll_panel_instr = new JScrollPane(panel_instr);
		scroll_panel_instr.setPreferredSize(new Dimension(inst_w, inst_h - 40));
		instp.add(scroll_panel_instr);
		instbox = new JComboBox[4 * cmdn];
		ix = new String[4096];
		for (int i = 0; i < 4096; i++)
			ix[i] = Integer.toString(i);
		for (int i = 0; i < cmdn; i++)
			for (int j = 0; j < 4; j++){
				if (j == 0) {
					instbox[i * 4 + j] = new JComboBox(inst);
				}
				else{
					instbox[i * 4 + j] = new JComboBox(nullx);					
				}
				instbox[i * 4 + j].addActionListener(this);
				panel_instr.add(instbox[i * 4 + j]);
			}
	
		
		//-----------------------------------------����״̬------------------------------------------
		int cdt_w = 600, cdt_h = 240;
		cdtp = new JPanel();
		//	cdtp.setLayout(new BoxLayout(cdtp, BoxLayout.Y_AXIS));
		cdtp.setLayout(new FlowLayout());
		cdtp.setPreferredSize(new Dimension(cdt_w, 240));
		getContentPane().add(cdtp);
//		cdtp.setVisible(false);
		cdtl = new JLabel("ָ��״̬");
		cdtl.setPreferredSize(new Dimension(label_w, 30));
		cdtp.add(cdtl);
		panel_cdt = new JPanel(new GridLayout(cmdn + 1, 4));
		panel_cdt.setPreferredSize(new Dimension(cdt_w - 20, (cmdn + 1) * 24));
		scroll_panel_cdt = new JScrollPane(panel_cdt);
		scroll_panel_cdt.setPreferredSize(new Dimension(cdt_w, cdt_h - 40));
		cdtp.add(scroll_panel_cdt);
		inst_cdtl = new JLabel[cmdn + 1][4];
		inst_cdtst = new String[cmdn + 1][4];
		for (int i = 0; i < cmdn + 1; i++) {
			for (int j = 0; j < 4; j++) {
				inst_cdtl[i][j] = new JLabel(inst_cdtst[i][j]);// string[][]
				inst_cdtl[i][j].setBorder(new EtchedBorder(EtchedBorder.LOWERED));
				panel_cdt.add(inst_cdtl[i][j]);
			}
		}
		
		//-----------------------------------------LOAD STORE------------------------------------------
		int ls_w = 350, ls_h = 240;
		loadp = new JPanel(new FlowLayout());
		loadp.setPreferredSize(new Dimension(ls_w, ls_h));
		getContentPane().add(loadp);
//		loadp.setVisible(false);
		loadl = new JLabel("Load Queue");
		loadl.setPreferredSize(new Dimension(label_w, 30));
		loadp.add(loadl);
		panel_load = new JPanel(new GridLayout(7, 4, 0, 0));
		panel_load.setPreferredSize(new Dimension(ls_w, ls_h-40));// 200,100
		panel_load.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		loadp.add(panel_load);
		inst_loadl = new JLabel[7][4];
		inst_loadst = new String[7][4];
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 4; j++) {
				inst_loadl[i][j] = new JLabel(inst_loadst[i][j]);
				inst_loadl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel_load.add(inst_loadl[i][j]);
			}
		}
		
		storep = new JPanel(new FlowLayout());
		storep.setPreferredSize(new Dimension(ls_w, ls_h));
		getContentPane().add(storep);
//		storep.setVisible(false);
		storel = new JLabel("Store Queue");
		storel.setPreferredSize(new Dimension(label_w, 30));
		storep.add(storel);
		panel_store = new JPanel(new GridLayout(7, 4, 0, 0));
		panel_store.setPreferredSize(new Dimension(ls_w, ls_h-40));// 200,100
		panel_store.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		storep.add(panel_store);
		inst_storel = new JLabel[7][4];
		inst_storest = new String[7][4];
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 4; j++) {
				inst_storel[i][j] = new JLabel(inst_storest[i][j]);
				inst_storel[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel_store.add(inst_storel[i][j]);
			}
		}
		
		//-----------------------------------------Reservation------------------------------------------
		
		int rsv_w = 965, rsv_h = 240;
		rsvp = new JPanel();
		rsvp.setLayout(new FlowLayout());
		rsvp.setPreferredSize(new Dimension(rsv_w, rsv_h));
		getContentPane().add(rsvp);
//		rsvp.setVisible(false);
		rsvl = new JLabel("Reservation");
		rsvl.setPreferredSize(new Dimension(label_w, 30));
		rsvp.add(rsvl);
		panel_rsv = new JPanel(new GridLayout(6, 7));
		panel_rsv.setPreferredSize(new Dimension(rsv_w, rsv_h-40));
		panel_rsv.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		rsvp.add(panel_rsv);
		inst_rsvl = new JLabel[6][8];
		inst_rsvst = new String[6][8];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				inst_rsvl[i][j] = new JLabel(inst_rsvst[i][j]);// string[][]
				inst_rsvl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel_rsv.add(inst_rsvl[i][j]);
			}
		}

		//-----------------------------------------Register------------------------------------------
		
		int reg_w = 900, reg_h = 240;
		regp = new JPanel();
		regp.setLayout(new FlowLayout());
		regp.setPreferredSize(new Dimension(reg_w, reg_h));
		getContentPane().add(regp);
//		regp.setVisible(false);
		regl = new JLabel("Register");
		regl.setPreferredSize(new Dimension(label_w, 30));
		regp.add(regl);
		panel_reg = new JPanel(new GridLayout(3, 12));
		panel_reg.setPreferredSize(new Dimension(reg_w, reg_h-40));
		panel_reg.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		regp.add(panel_reg);
		inst_regl = new JLabel[3][12];
		inst_regst = new String[3][12];
		inst_regt = new JTextField[3][12];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 12; j++) {
				if (i<2 || j == 0){
					inst_regl[i][j] = new JLabel(inst_regst[i][j]);// string[][]
					inst_regl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
					panel_reg.add(inst_regl[i][j]);					
				}
				else{
					inst_regt[i][j] = new JTextField(inst_regst[i][j]);
					inst_regt[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
					inst_regt[i][j].setFont(new Font("г��",Font.BOLD|Font.ITALIC,16));
					panel_reg.add(inst_regt[i][j]);
				}
			}
		}
		
		//-----------------------------------------Memory------------------------------------------
		int mem_w = 410, mem_h = 240;
		memp = new JPanel();
		memp.setLayout(new FlowLayout());
		memp.setPreferredSize(new Dimension(mem_w, mem_h));
		getContentPane().add(memp);
//		memp.setVisible(false);
		meml = new JLabel("Memory");
		meml.setPreferredSize(new Dimension(label_w, 30));
		memp.add(meml);
		panel_mem = new JPanel(new GridLayout(2, 6));
		panel_mem.setPreferredSize(new Dimension(mem_w, mem_h-40));
		panel_mem.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		memp.add(panel_mem);
		inst_meml = new JLabel[2][6];
		inst_memst = new String[2][6];
		inst_memt = new JTextField[2][6];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 6; j++){
				if (i==0 || j==0){
					inst_meml[i][j] = new JLabel(inst_memst[i][j]);// string[][]
					inst_meml[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
					panel_mem.add(inst_meml[i][j]);
				}
				else {
					inst_memt[i][j] = new JTextField(inst_memst[i][j]);
					inst_memt[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
					inst_memt[i][j].setFont(new Font("г��",Font.BOLD|Font.ITALIC,16));
					panel_mem.add(inst_memt[i][j]);
				}				
			}
		}
		
		
		stinit();
		display();
	}
	
	
	
	
	private Main() {
		backend();
		
		
		FlowLayout all_layout = new FlowLayout(FlowLayout.LEFT);
		getContentPane().setLayout(all_layout);

		int but_w = 1400, but_h = 50;
		
		//-----------------------------------------���ܰ�ť------------------------------------------	
		panel_but = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel_but.setPreferredSize(new Dimension(but_w, but_h));
		getContentPane().add(panel_but);
		stepbut = new JButton("ִ��һ��");
		stepbut.addActionListener(this);
		stepbut.setEnabled(false);
		startbut = new JButton("��ʼ");
		startbut.addActionListener(this);
		resetbut = new JButton("����");
		resetbut.addActionListener(this);
		lookupbut = new JButton("�ڴ��ѯ");
		lookupbut.addActionListener(this);
		memaddr = new JTextField(10);
		command_numbers = new JTextField(5);
		command_numbers.setText("10");
		cmdl = new JLabel("��������: ");
		savebut = new JButton("�ڴ汣��");
		savebut.addActionListener(this);
		stepnbut = new JButton("�Զ�ִ��");
		stepnbut.addActionListener(this);
		stepnbut.setEnabled(false);
		regsavebut = new JButton("�Ĵ�������");
		regsavebut.addActionListener(this);
		filebut = new JButton("�ļ�����");
		filebut.addActionListener(this);
		
		
		panel_but.add(cmdl);
		panel_but.add(command_numbers);
		panel_but.add(resetbut);
		panel_but.add(filebut);
		panel_but.add(startbut);
		panel_but.add(stepbut);
		panel_but.add(stepnbut);
		panel_but.add(lookupbut);
		panel_but.add(memaddr);
		panel_but.add(savebut);
		panel_but.add(regsavebut);
		
		//-----------------------------------------�㷨����------------------------------------------	
		cmdn = 10;
		window_init();
		
		setSize(1400, 900);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	
	private void backend() {
		fpu = new FloatPointUnit();
		for(int i = 0; i < 11; ++i) {
			fpu.setReg(i, 0);
		}
		for (int i = 0; i<4096; i++)
			fpu.memory[i] = 0.0f;
	}
	
	public static void main(String[] args) {
		new Main();
	}





	private void window_clear() {
		cdtp.setVisible(false);
		instp.setVisible(false);
		loadp.setVisible(false);
		memp.setVisible(false);
		storep.setVisible(false);
		regp.setVisible(false);
		rsvp.setVisible(false);
		getContentPane().remove(cdtp);
		getContentPane().remove(instp);
		getContentPane().remove(loadp);
		getContentPane().remove(memp);
		getContentPane().remove(storep);
		getContentPane().remove(regp);
		getContentPane().remove(rsvp);
		getContentPane().invalidate();
		getContentPane().validate();
	}
	private void display() {
		int cmdnn = cmdn + 1;
		for (int i = 0; i < cmdnn; i++)
			for (int j = 0; j < 4; j++) {
				inst_cdtl[i][j].setText(inst_cdtst[i][j]);
			}
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 4; j++) {
				inst_loadl[i][j].setText(inst_loadst[i][j]);
			}
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 4; j++) {
				inst_storel[i][j].setText(inst_storest[i][j]);
			}
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 8; j++) {
				inst_rsvl[i][j].setText(inst_rsvst[i][j]);
			}
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 12; j++) {
				if (i<2 || j==0)
					inst_regl[i][j].setText(inst_regst[i][j]);
				else
					inst_regt[i][j].setText(inst_regst[i][j]);
			}
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 6; j++) {
				if (i==0 || j==0)
					inst_meml[i][j].setText(inst_memst[i][j]);
				else if (i==1)
					inst_memt[i][j].setText(inst_memst[i][j]);
			}
	}

	void stinit(){
		inst_cdtst[0][0] = "ָ��";
		inst_cdtst[0][1] = "����";
		inst_cdtst[0][2] = "ִ��";
		inst_cdtst[0][3] = "д��";
	
		inst_loadst[0][0] = "����";
		inst_loadst[0][1] = "Busy";
		inst_loadst[0][2] = "Address";
		inst_loadst[0][3] = "Cache";
		inst_loadst[1][0] = "Load1";
		inst_loadst[2][0] = "Load2";
		inst_loadst[3][0] = "Load3";
		inst_loadst[4][0] = "Load4";
		inst_loadst[5][0] = "Load5";
		inst_loadst[6][0] = "Load6";
		inst_loadst[1][1] = "no";
		inst_loadst[2][1] = "no";
		inst_loadst[3][1] = "no";
		inst_loadst[4][1] = "no";
		inst_loadst[5][1] = "no";
		inst_loadst[6][1] = "no";
		
		inst_storest[0][0] = "����";
		inst_storest[0][1] = "Busy";
		inst_storest[0][2] = "Address";
		inst_storest[0][3] = "Qi";
		inst_storest[1][0] = "Store1";
		inst_storest[2][0] = "Store2";
		inst_storest[3][0] = "Store3";
		inst_storest[4][0] = "Store4";
		inst_storest[5][0] = "Store5";
		inst_storest[6][0] = "Store6";
		inst_storest[1][1] = "no";
		inst_storest[2][1] = "no";
		inst_storest[3][1] = "no";
		inst_storest[4][1] = "no";
		inst_storest[5][1] = "no";
		inst_storest[6][1] = "no";
		
		
		inst_rsvst[0][0] = "Time";
		inst_rsvst[0][1] = "����";
		inst_rsvst[0][2] = "Busy";
		inst_rsvst[0][3] = "Op";
		inst_rsvst[0][4] = "Qj";
		inst_rsvst[0][5] = "Qk";
		inst_rsvst[0][6] = "Vj";
		inst_rsvst[0][7] = "Vk";
		inst_rsvst[1][1] = "Add0";
		inst_rsvst[2][1] = "Add1";
		inst_rsvst[3][1] = "Add2";
		inst_rsvst[4][1] = "Mult0";
		inst_rsvst[5][1] = "Mult1";
		inst_rsvst[1][2] = "no";
		inst_rsvst[2][2] = "no";
		inst_rsvst[3][2] = "no";
		inst_rsvst[4][2] = "no";
		inst_rsvst[5][2] = "no";
		
		inst_regst[0][0] = "�ֶ�";
		inst_regst[1][0] = "״̬";
		inst_regst[2][0] = "ֵ";
		String[] regs = fpu.getRegInfo().split("\n");
		for (int i = 1; i < 12; i++) {
			inst_regst[0][i] = fx[i - 1];
			String[] row = regs[i-1].split("\t");
			inst_regst[1][i] = row[2];
			inst_regst[2][i] = row[1];
			
		}
		
		
		inst_memst[0][0] = "��ַ";
		inst_memst[1][0] = "ֵ";
		
		memaddr.setText("0");
		mempos = 0;
		for (int i = 0; i<5; i++){
			inst_memst[0][i+1] = Integer.toString(i);
			inst_memst[1][i+1] = Float.toString(fpu.memory[i]);
		}
	}

	private void preparest() {
	
		//-----------------------------------------����״̬------------------------------------------	
		System.out.println("enter preparest");
		int cmdnn = cmdn + 1;
		Instruction tmpi = null;
		allinst = new ArrayList<Instruction>();
		for (int i = 1; i < cmdnn; i++)
			for (int j = 0; j < 4; j++) {
				if (j == 0) {
					int temp = (i - 1) * 4;
					String disp;
					disp = inst[instbox[temp].getSelectedIndex()] + "    ";
					if (instbox[temp].getSelectedIndex() == 0){
						disp = "";
					}
					else if (instbox[temp].getSelectedIndex() == 1 
							|| instbox[temp].getSelectedIndex() == 2) {
						disp = disp
								+ fx[instbox[temp + 1].getSelectedIndex()]
								+ ",  "
								+ ix[instbox[temp + 2].getSelectedIndex()]
								;
						tmpi = new Instruction(op(instbox[temp].getSelectedIndex()), fx[instbox[temp + 1].getSelectedIndex()], Integer.valueOf(ix[instbox[temp + 2].getSelectedIndex()]));
						fpu.addInstruction(tmpi);
						allinst.add(tmpi);
					} else {
						disp = disp
								+ fx[instbox[temp + 1].getSelectedIndex()]
								+ ",  "
								+ fx[instbox[temp + 2].getSelectedIndex()]
								+ ",  "
								+ fx[instbox[temp + 3].getSelectedIndex()];
						tmpi = new Instruction(op(instbox[temp].getSelectedIndex()), fx[instbox[temp + 1].getSelectedIndex()], fx[instbox[temp + 2].getSelectedIndex()], fx[instbox[temp + 3].getSelectedIndex()]);
						fpu.addInstruction(tmpi);
						allinst.add(tmpi);
					}
					
					inst_cdtst[i][j] = disp;
				}// if(j==0)
				else
					inst_cdtst[i][j] = "";
			}
		
		
	}

	private OP op(int op) {

		System.out.println("enter OP fun" + inst[op]);
		if (op == 1)
			return OP.ST;
		if (op == 2)
			return OP.LD;
		if (op == 3)
			return OP.ADDD;
		if (op == 4)
			return OP.SUBD;
		if (op == 5)
			return OP.MULD;
		if (op == 6)
			return OP.DIVD;
		return null;
	}




	//-----------------------------------------Action------------------------------------------
	
	@Override
	public void actionPerformed(ActionEvent e) {

		//-----------------------------------------ָ�����------------------------------------------	
		actionInstbox(e);
		
		//-----------------------------------------��ʼ��ť------------------------------------------	
		actionStart(e);
		
		//-----------------------------------------ִ��------------------------------------------
		actionStep(e);
		actionStepN(e);
		
		//-----------------------------------------���ð�ť------------------------------------------	
		actionReset(e);

		//-----------------------------------------�ڴ水ť------------------------------------------	
		actionLookup(e);
		actionMemSave(e);
		
		//-----------------------------------------�Ĵ�����ť------------------------------------------
		actionRegsave(e);
		
		//-----------------------------------------���밴ť------------------------------------------
		actionFile(e);
		
	}




	private void actionFile(ActionEvent e) {
	
		if (e.getSource() == filebut){
			fileChooser = new JFileChooser();
			int selected = fileChooser.showOpenDialog(getContentPane());
			if (selected == JFileChooser.APPROVE_OPTION){
				File file = fileChooser.getSelectedFile();
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String row = null;
					ArrayList<String> rows = new ArrayList<String>();
					while ((row = reader.readLine()) != null){
						System.out.println(row);
						rows.add(row);
					}
					cmdn = rows.size();
					if (cmdn < 10)
						cmdn = 10;
					command_numbers.setText(Integer.toString(cmdn));
					reset_cmd();
					int p = 0;
					for (int i = 0; i<rows.size(); i++, p+=4){
						for (int j = 0; j < 4; j++)
							instbox[p+j].removeAllItems();
						String[] line = rows.get(i).split(" ");
						if (line.length != 3 && line.length != 4){
							JOptionPane.showMessageDialog(this, "�ļ���ʽ����", "�ļ���ʽ����", JOptionPane.PLAIN_MESSAGE);
							return;
						}
						for (int j = 0; j<line.length; j++){
							boolean flag = false;
							for (int k = 0; k<inst.length;k++)
								if (line[j].equals(inst[k])){
									for (int kk = 0; kk < inst.length; kk++)
										instbox[p+j].addItem(inst[kk]);
									
									instbox[p+j].setSelectedIndex(k);
									flag = true;
									break;
								}
							if (flag)
								continue;
							for (int k = 0; k<fx.length;k++)
								if (line[j].equals(fx[k])){
									
									for (int kk = 0; kk < fx.length; kk++)
										instbox[p+j].addItem(fx[kk]);
									
									instbox[p+j].setSelectedIndex(k);
									flag = true;
									break;
								}
							if (flag)
								continue;
							for (int k = 0; k<ix.length;k++)
								if (line[j].equals(ix[k])){
									
									for (int kk = 0; kk < ix.length; kk++)
										instbox[p+j].addItem(ix[kk]);
									
									instbox[p+j].setSelectedIndex(k);
									flag = true;
									break;
								}
						}
					}
					reader.close();
				} catch (IOException e1) {
			
					e1.printStackTrace();
				}
				
			}


		}
	}




	private void actionRegsave(ActionEvent e) {

		if (e.getSource() == regsavebut){
			for (int i = 0; i<11; i++){
				inst_regst[2][i+1] = inst_regt[2][i+1].getText();
				float v = 0;
				try {
					v = Float.valueOf(inst_regt[2][i+1].getText());
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(this, "���벻������", "���벻������", JOptionPane.PLAIN_MESSAGE);
				}
				
				fpu.setReg(i, v);
			}
			display();
		}
	}




	private void actionMemSave(ActionEvent e) {
	
		if (e.getSource() == savebut){
			int pos = mempos;
			for (int i = 1; i<6; i++){				
				
				inst_memst[0][i] = Integer.toString(pos + i - 1);
				inst_memst[1][i] = inst_memt[1][i].getText();
				try {
					fpu.memory[pos + i - 1] = Float.valueOf(inst_memt[1][i].getText());				
					
				} catch (Exception e2) {
					
					fpu.memory[pos + i - 1] = 0.0f;		
					JOptionPane.showMessageDialog(this, "���벻������", "���벻������", JOptionPane.PLAIN_MESSAGE);

				}
			}
			display();
		}
	}




	private void actionLookup(ActionEvent e) {
		
		if (e.getSource() == lookupbut){
			try {
				mempos = Integer.valueOf( memaddr.getText());
				
			} catch (Exception e2) {
				mempos = 0;
				JOptionPane.showMessageDialog(this, "���벻������", "���벻������", JOptionPane.PLAIN_MESSAGE);
			}
			if (mempos<0 || mempos>4091)
				mempos=0;
			memaddr.setText(Integer.toString(mempos));
			
			for (int i = 1; i<6; i++){
//				inst_meml[0][i].setText(Integer.toString(pos + i - 1));
//				inst_memt[1][i].setText(Float.toString(fpu.memory[pos + i - 1]));				
				inst_memst[0][i]=Integer.toString(mempos + i - 1);
				inst_memst[1][i]=Float.toString(fpu.memory[mempos + i - 1]);				
				
			}
			display();
		}
	}




	void actionInstbox(ActionEvent e){
		
		int cmdnn = cmdn * 4;
		for (int i = 0; i < cmdnn; i = i + 4) {//
			if (e.getSource() == instbox[i]) {
				if (instbox[i].getSelectedIndex() == 0){
					instbox[i + 1].removeAllItems();
					instbox[i + 2].removeAllItems();
					instbox[i + 3].removeAllItems();
				}
				else if (instbox[i].getSelectedIndex() == 1 
						|| instbox[i].getSelectedIndex() == 2) {
					instbox[i + 1].removeAllItems();
					for (int j = 0; j < fx.length; j++)
						instbox[i + 1].addItem(fx[j]);
					instbox[i + 2].removeAllItems();
					for (int j = 0; j < ix.length; j++)
						instbox[i + 2].addItem(ix[j]);
					instbox[i + 3].removeAllItems();
					for (int j = 0; j < nullx.length; j++)
						instbox[i + 3].addItem(nullx[j]);
				} else {
					instbox[i + 1].removeAllItems();
					for (int j = 0; j < fx.length; j++)
						instbox[i + 1].addItem(fx[j]);
					instbox[i + 2].removeAllItems();
					for (int j = 0; j < fx.length; j++)
						instbox[i + 2].addItem(fx[j]);
					instbox[i + 3].removeAllItems();
					for (int j = 0; j < fx.length; j++)
						instbox[i + 3].addItem(fx[j]);
				}// if-else
			}// if
		}// for
	}
	
	void reset_cmd(){
		try {
			cmdn = Integer.valueOf(command_numbers.getText());
			if (cmdn < 10)
				cmdn = 10;
			command_numbers.setText(Integer.toString(cmdn));
		} catch (Exception e) {
			cmdn = 10;
			JOptionPane.showMessageDialog(this, "���벻������", "���벻������", JOptionPane.PLAIN_MESSAGE);

		}
		startbut.setEnabled(true);
		startbut.setEnabled(true);
		savebut.setEnabled(true);
		regsavebut.setEnabled(true);
		
		filebut.setEnabled(true);
		stepbut.setEnabled(false);
		stepnbut.setEnabled(false);
		window_clear();
		window_init();
	}

	private void actionReset(ActionEvent e) {
		
		if (e.getSource() ==resetbut) {
			reset_cmd();
			fpu.reset();
			display();
		}
	}




	private void actionStart(ActionEvent e) {
		
		int cmdnn = cmdn * 4;
		if (e.getSource() == startbut) {
			for (int i = 0; i < cmdnn; i++)
				instbox[i].setEnabled(false);
			setTn(0);
			stepbut.setEnabled(true);
			stepnbut.setEnabled(true);
			startbut.setEnabled(false);
			savebut.setEnabled(false);
			regsavebut.setEnabled(false);
			
			filebut.setEnabled(false);
			for (int i = 1; i<12; i++)
				inst_regt[2][i].setEditable(false);
			for (int i = 1; i<6; i++)
				inst_memt[1][i].setEditable(false);

			// ����ָ�����õ�ָ���ʼ�����������
			preparest();
			// չʾ�������
			display();
		}
		
	}
	
	private void actionStepN(ActionEvent e){
		if (e.getSource() == stepnbut){
			Thread thread = new Thread(new Runnable() {
				public void run() {
					while (! fpu.finishExcute()){
						fpu.update();
						setTn(getTn() + 1);
						update();
						display();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
						
							e1.printStackTrace();
						}		
					}
					JOptionPane.showMessageDialog(null, "ִ�����", "ִ�����", JOptionPane.PLAIN_MESSAGE);
				}
			});
			thread.start();
			/*while (! fpu.finishExcute()){
				fpu.update();
				Tn++;
				update();
				display();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				
					e1.printStackTrace();
				}		
			}*/
		}
	}
	
	private void update(){
		String[] res = fpu.showReserStations().split("\n");
		
		int r = 1;
		for (int i = 0; i<6; i++, r++){
			String[] row = res[i].split("\t");
			while (row.length < 7){
				i++;
				row = res[i].split("\t");
			}
			
			inst_rsvst[r][2] = row[1];
			inst_rsvst[r][3] = row[2];
			inst_rsvst[r][4] = row[3];
			inst_rsvst[r][5] = row[4];
			inst_rsvst[r][6] = row[5];
			inst_rsvst[r][7] = row[6];				
		}
		
	
		int rl = 1, rs = 1;
		for (int i = 6; i<13; i++){
			String[] row = res[i].split("\t");
			while (row.length < 5){
				i++;
				row = res[i].split("\t");
			}
		
			if (row[2].equals("ST")){
				inst_storest[rs][1] = row[1];
				inst_storest[rs][2] = row[3];
				inst_storest[rs][3] = row[4];
				rs++;
			}
			else{
				inst_loadst[rl][1] = row[1];
				inst_loadst[rl][2] = row[3];
				inst_loadst[rl][3] = row[4];
				rl++;
			}
		}
		
		
		String[] regs = fpu.getRegInfo().split("\n");
		for (int i = 1; i < 12; i++) {
			String[] row = regs[i-1].split("\t");
			inst_regst[1][i] = row[2];
			inst_regst[2][i] = row[1];
			
		}
		
		for (int i = 0; i<5; i++)
			inst_memst[1][1+i] = Float.toString(fpu.getMem(i+mempos));
					
		r=1;
		for (int i = 0,j=allinst.size(); i<j;i++,r++){
			int v = allinst.get(i).getInstStatus();
			if (v == -1){
				inst_cdtst[r][1] = "";
				inst_cdtst[r][2] = "";
				inst_cdtst[r][3] = "";
			}
			else if (v >= 0){
				inst_cdtst[r][1] = "OK";
				inst_cdtst[r][2] = Integer.toString(v);
				inst_cdtst[r][3] = "";
			}
			else if (v == -2){
				inst_cdtst[r][1] = "OK";
				inst_cdtst[r][2] = "OK";
				inst_cdtst[r][3] = "OK";
			}
		}
		
	}

	private void actionStep(ActionEvent e) {
	
		if (e.getSource() == stepbut){
			System.out.println("enter actstep");
			fpu.update();
			setTn(getTn() + 1);
			
			update();
			display();
			if (fpu.finishExcute())
				 JOptionPane.showMessageDialog(this, "ִ�����", "ִ�����", JOptionPane.PLAIN_MESSAGE);
		}
	}




	public int getTn() {
		return Tn;
	}




	public void setTn(int tn) {
		Tn = tn;
	}
}
