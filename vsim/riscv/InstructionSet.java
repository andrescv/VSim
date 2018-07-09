/*
Copyright (C) 2018 Andres Castellanos

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>
*/

package vsim.riscv;

import vsim.utils.IO;
import java.util.HashMap;
import vsim.utils.Message;
import vsim.utils.Colorize;
import java.lang.reflect.Modifier;
import vsim.riscv.instructions.Format;
import vsim.riscv.instructions.Instruction;


/**
 * The class InstructionSet represents the available instruction set.
 */
public final class InstructionSet {

  /** r-type instructions package */
  private static final String RTYPE  = "vsim.riscv.instructions.rtype";
  /** i-type instructions package */
  private static final String ITYPE  = "vsim.riscv.instructions.itype";
  /** s-type instructions package */
  private static final String STYPE  = "vsim.riscv.instructions.stype";
  /** b-type instructions package */
  private static final String BTYPE  = "vsim.riscv.instructions.btype";
  /** u-type instructions package */
  private static final String UTYPE  = "vsim.riscv.instructions.utype";
  /** j-type instructions package */
  private static final String JTYPE  = "vsim.riscv.instructions.jtype";
  /** r4-type instructions package */
  private static final String R4TYPE = "vsim.riscv.instructions.r4type";

  /** current classes in rtype package */
  private static final String[] RClasses = {
    "Add", "Sub", "Sll",
    "Slt", "Sltu", "Xor",
    "Srl", "Sra", "Or",
    "And", "Div", "Divu",
    "Mul", "Mulh", "Mulhu",
    "Mulhsu", "Rem", "Remu",
    "Fmvwx", "Fmvxw", "Fcvtsw",
    "Fcvtswu", "Fcvtws", "Fcvtwus",
    "Fadds", "Fsubs", "Fmuls",
    "Fdivs", "Fsqrts", "Fsgnjs",
    "Fsgnjns", "Fsgnjxs", "Feqs",
    "Flts", "Fles", "Fclasss",
    "Fmins", "Fmaxs"
  };

  /** current classes in itype package */
  private static final String[] IClasses = {
    "Jalr", "Lb", "Lh",
    "Lw", "Lbu", "Lhu",
    "Addi", "Slti", "Sltiu",
    "Xori", "Ori", "Andi",
    "Slli", "Srli", "Srai",
    "Ecall", "Flw", "Ebreak"
  };

  /** current classes in stype package */
  private static final String[] SClasses = {
    "Sb", "Sh", "Sw",
    "Fsw"
  };

  /** current classes in btype package */
  private static final String[] BClasses = {
    "Beq", "Bge", "Bgeu",
    "Blt", "Bltu", "Bne"
  };

  /** current classes in utype package */
  private static final String[] UClasses = {
    "Auipc", "Lui"
  };

  /** current classes in jtype package */
  private static final String[] JClasses = {
    "Jal"
  };

  /** current classes in r4type package */
  private static final String[] R4Classes = {
    "Fmadds", "Fmsubs",
    "Fnmadds", "Fnmsubs"
  };

  /** the only available instance of the InstructionSet class */
  public static final InstructionSet insts = new InstructionSet();

  /** instructions dictionary */
  private HashMap<String, Instruction> instructions;

  /**
   * Unique constructor that initializes a newly InstructionSet object.
   *
   * @see vsim.riscv.instructions.Instruction
   */
  private InstructionSet() {
    this.instructions = new HashMap<String, Instruction>();
    this.populate();
  }

  /**
   * This method adds all the instruction classes in the given package.
   *
   * @param classes the classes to add
   * @param pkg the package of the classes
   * @see vsim.riscv.instructions.Instruction
   */
  private void add(String[] classes, String pkg) {
    for(String className: classes) {
      String classPath = pkg + "." + className;
      try {
        Class<?> cls = Class.forName(classPath);
        // only final classes
        if (!Instruction.class.isAssignableFrom(cls) ||
          Modifier.isAbstract(cls.getModifiers()) ||
          Modifier.isInterface(cls.getModifiers()))
          continue;
        // add this new instruction to isa
        Instruction inst = (Instruction)(cls.getConstructor().newInstance());
        String mnemonic = inst.getMnemonic();
        if (this.instructions.containsKey(mnemonic))
          Message.warning("duplicated instruction name: '" + mnemonic + "', skip this");
        else
          this.instructions.put(mnemonic, inst);
      } catch (Exception e) {
        Message.panic("class: '" + classPath + "' could not be loaded");
      }
    }
  }

  /**
   * This method populates the instruction set instance with all
   * the instructions available.
   *
   * @see vsim.riscv.instructions.Instruction
   */
  private void populate() {
    this.add(RClasses, RTYPE);
    this.add(IClasses, ITYPE);
    this.add(SClasses, STYPE);
    this.add(BClasses, BTYPE);
    this.add(UClasses, UTYPE);
    this.add(JClasses, JTYPE);
    this.add(R4Classes, R4TYPE);
  }

  /**
   * This method returns the intruction that represent the given mnemonic.
   *
   * @param mnemonic the instruction mnemonic
   * @see vsim.riscv.instructions.Instruction
   * @return the instruction or null if the mnemonic is invalid
   */
  public Instruction get(String mnemonic) {
    return this.instructions.get(mnemonic);
  }

  /**
   * This method pretty prints the usage and description of an instruction.
   *
   * @param mnemonic instruction mnemonic to print
   */
  public void print(String mnemonic) {
    mnemonic = mnemonic.toLowerCase();
    Instruction inst = this.instructions.get(mnemonic);
    if (inst != null) {
      IO.stdout.println("Instruction:");
      IO.stdout.println();
      IO.stdout.println(
        String.format(
          "[%s] (%s) example: %s",
          Colorize.red(inst.getFormat().toString()),
          Colorize.green(mnemonic),
          Colorize.cyan(inst.getUsage())
        )
      );
      IO.stdout.println();
      IO.stdout.println("Description:");
      IO.stdout.println();
      IO.stdout.println(inst.getDescription());
    } else
      Message.warning("Invalid instruction mnemonic: '" + mnemonic + "'");
  }

  /**
   * This method pretty prints the instruction set.
   */
  public void print() {
    // number of instructions
    IO.stdout.println(
      String.format(
        "Number of Instructions: %s",
        Colorize.green(String.format("%03d", this.instructions.size()))
      )
    );
    IO.stdout.println();
    IO.stdout.println(Colorize.purple("FORMAT   MNEMONIC                USAGE"));
    IO.stdout.println();
    int maxLength = -1;
    // get mnemonic max instruction length for pretty printer
    for (String mnemonic: this.instructions.keySet())
      maxLength = Math.max(maxLength, mnemonic.length());
    // get all formats
    Format[] formats = Format.values();
    // overhead here but necessary to print instructions in order
    for (int i = 0; i < formats.length; i++) {
      for (String mnemonic: this.instructions.keySet()) {
        Instruction inst = this.instructions.get(mnemonic);
        Format format = inst.getFormat();
        String usage = inst.getUsage();
        String space = "";
        for (int j = 0; j < (maxLength - mnemonic.length()); j++)
          space += " ";
        if (format == formats[i]) {
          IO.stdout.println(
            String.format(
              " [%s]%s  (%s)%s example: %s",
              Colorize.red(format.toString()),
              (format.toString().length() == 2) ? " " : "  ",
              Colorize.green(mnemonic),
              space,
              Colorize.cyan(usage)
            )
          );
        }
      }
    }
  }

}
