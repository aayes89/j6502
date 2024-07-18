package j6502emul;

/**
 *
 * @author Slam
 */
public class CPU6502 {

    // Tipos de datos
    public static class StatusFlags {

        public int C;   // 0: Carry Flag
        public int Z;   // 1: Zero Flag
        public int I;   // 2: Interrupt disable
        public int D;   // 3: Decimal mode
        public int B;   // 4: Break
        public int Unused; // 5: Unused
        public int V;   // 6: Overflow
        public int N;   // 7: Negative

        public String getStatus() {
            return "StatusFlags{" + "C=" + C + ", Z=" + Z + ", I=" + I + ", D=" + D + ", B=" + B + ", Unused=" + Unused + ", V=" + V + ", N=" + N + '}';
        }

    }

    // Memoria
    public static class Mem {

        public static final int MAX_MEM = 65536; //1024 * 64; // 64kbs
        public byte[] Data = new byte[MAX_MEM];

        public void Initialise() {
            for (int i = 0; i < MAX_MEM; i++) {
                Data[i] = 0;
            }
        }

        public byte read(int address) {
            // Aquí podrías agregar una validación si address >= MAX_MEM
            return Data[address];
        }

        public void write(int address, byte value) {
            // Aquí podrías agregar una validación si address >= MAX_MEM
            Data[address] = value;
        }
    }

    public static class Opcode {

        public static final byte LDA = (byte) 0xA9;             //LDA #10 ;Load 10 ($0A) into the accumulator
        public static final byte zero_page = (byte) 0xA5;              //
        public static final byte zero_page_x = (byte) 0xB5;             //
        public static final byte absolute = (byte) 0xAD;             //
        public static final byte absolute_x = (byte) 0xBD;             //
        public static final byte absolute_y = (byte) 0xB9;             //
        public static final byte BRK = (byte) 0x00;             //BRK - Force Interrupt
        public static final byte ADC = (byte) 0x69;             //ADD with carry
        public static final byte AND = (byte) 0x29;             //AND - Logical AND
        public static final byte BCC = (byte) 0x90;             //BCC - Branch if Carry Clear
        public static final byte BCS = (byte) 0xB0;             //BCS - Branch if Carry Set
        public static final byte BEQ = (byte) 0xF0;             //BEQ - Branch if Equal
        public static final byte BIT = (byte) 0x24;             //BIT - Bit Test. A & M, N = M7, V = M6
        public static final byte BMI = (byte) 0x30;             //BMI - Branch if Minus
        public static final byte BNE = (byte) 0xD0;             //BNE - Branch if Not Equal
        public static final byte BPL = (byte) 0x10;             //BPL - Branch if Positive
        public static final byte BVC = (byte) 0x50;             //BVC - Branch if Overflow Clear
        public static final byte BVS = (byte) 0x70;             //BVS - Branch if Overflow Set
        public static final byte CLC = (byte) 0x18;             //CLC - Clear Carry Flag
        public static final byte CLD = (byte) 0xD8;             //CLD - Clear Decimal Mode
        public static final byte CLI = (byte) 0x58;             //CLI - Clear Interrupt Disable
        public static final byte CLV = (byte) 0xB8;             //CLV - Clear Overflow Flag
        public static final byte CMP = (byte) 0xC9;             //CMP - Compare Z,C,N = A-M
        public static final byte CPX = (byte) 0xE0;             //CPX - Compare X Register Z,C,N = X-M
        public static final byte CPY = (byte) 0xC0;             //CPY - Compare Y Register Z,C,N = Y-M
        public static final byte DEC = (byte) 0xC6;             //DEC - Decrement Memory M,Z,N = M-1
        public static final byte DEX = (byte) 0xCA;             //DEX - Decrement X Register X,Z,N = X-1
        public static final byte DEY = (byte) 0x88;             //DEY - Decrement Y Register Y,Z,N = Y-1
        public static final byte EOR = (byte) 0x49;             //EOR - Exclusive OR A,Z,N = A^M
        public static final byte INC = (byte) 0xE6;             //INC - Increment Memory M,Z,N = M+1
        public static final byte INX = (byte) 0xE8;             //INX - Increment X Register X,Z,N = X+1
        public static final byte INY = (byte) 0xC8;             //INY - Increment Y Register Y,Z,N = Y+1
        public static final byte JMP = (byte) 0x4C;             //JMP - Jump
        public static final byte JSR = (byte) 0x20;             //JSR - Jump to Subroutine
        public static final byte NOP = (byte) 0xEA;             //NOP - No Operation
        public static final byte ORA = (byte) 0x09;             //ORA - Logical Inclusive OR A,Z,N = A|M
        public static final byte PHA = (byte) 0x48;             //PHA - Push Accumulator
        public static final byte PHP = (byte) 0x08;             //PHP - Push Processor Status
        public static final byte PLA = (byte) 0x68;             //PLA - Pull Accumulator
        public static final byte PLP = (byte) 0x28;             //PLP - Pull Processor Status
        public static final byte ROL = (byte) 0x2A;             //ROL A ;Rotate left one bit
        public static final byte ROR = (byte) 0x6A;             //ROR A ;Rotate right one bit
        public static final byte RTI = (byte) 0x40;             //RTI - Return from Interrupt
        public static final byte RTS = (byte) 0x60;             //RTS - Return from Subroutine
        public static final byte SBC = (byte) 0xE9;             //SBC - Subtract with Carry A,Z,C,N = A-M-(1-C)
        public static final byte SEC = (byte) 0x38;             //SEC - Set Carry Flag
        public static final byte SED = (byte) 0xF8;             //SED - Set Decimal Flag
        public static final byte SEI = (byte) 0x78;             //SEI - Set Interrupt Disable
        public static final byte STA = (byte) 0x85;             //STA - Store AccumulatorSTA - Store Accumulator
        public static final byte STX = (byte) 0x86;             //STX - Store X Register
        public static final byte STY = (byte) 0x84;             //STY - Store Y Register
        public static final byte TAX = (byte) 0xAA;             //TAX - Transfer Accumulator to X
        public static final byte TAY = (byte) 0xA8;             //TAY - Transfer Accumulator to Y
        public static final byte TSX = (byte) 0xBA;             //TSX - Transfer Stack Pointer to X
        public static final byte TXA = (byte) 0x8A;             //TXA - Transfer X to Accumulator
        public static final byte TXS = (byte) 0x9A;             //TXS - Transfer X to Stack Pointer
        public static final byte TYA = (byte) 0x98;             //TYA - Transfer Y to Accumulator
        public static final byte LSR = (byte) 0x4A;             //LSR A ;Logical shift right one bit 
        public static final byte LDX = (byte) 0xA2;             //LDX #LO LABEL ;Load the LSB of a 16 bit address into X
        public static final byte LDY = (byte) 0xA0;             //LDY #HI LABEL ;Load the MSB of a 16 bit address into Y
        public static final byte ASL = (byte) 0x0A;             //ASL ANSWER ;(ASL - Arithmetic Shift Left) Shift labelled location ANSWER left (A,Z,C,N = M*2 or M,Z,C,N = M*2)

    }

    // CPU
    public static class xCPU {

        private int PC;     // Program counter
        private byte SP;    // Stack pointer
        private byte A, X, Y; // Registers
        private StatusFlags Flag; // Processor status
        private byte status; // Registro de estado

        public void reset(Mem memory) {
            //reset(0xFFFC, memory);
            reset(0x0, memory);
        }

        public void reset(int resetVector, Mem memory) {
            PC = resetVector;
            SP = (byte) 0xFF;
            Flag = new StatusFlags();
            Flag.C = Flag.Z = Flag.I = Flag.D = Flag.B = Flag.V = Flag.N = 0;
            A = X = Y = 0;
            memory.Initialise();
        }

        public byte fetchByte(int cycles, Mem memory) {
            byte data = memory.read(PC);
            PC++;
            cycles--;
            return data;
        }

        public byte fetchSByte(int cycles, Mem memory) {
            return fetchByte(cycles, memory);
        }

        public int fetchWord(int cycles, Mem memory) {
            // 6502 is little endian
            int data = memory.read(PC);
            PC++;

            data |= (memory.read(PC) << 8);
            PC++;

            cycles -= 2;
            return data;
        }

        public byte readByte(int cycles, int address, Mem memory) {
            byte data = memory.read(address);
            cycles--;
            return data;
        }

        public int readWord(int cycles, int address, Mem memory) {
            byte loByte = readByte(cycles, address, memory);
            byte hiByte = readByte(cycles, address + 1, memory);
            return (loByte & 0xFF) | ((hiByte & 0xFF) << 8);
        }

        public void writeByte(byte value, int cycles, int address, Mem memory) {
            memory.write(address, value);
            cycles--;
        }

        public void writeWord(int value, int cycles, int address, Mem memory) {
            memory.write(address, (byte) (value & 0xFF));
            memory.write(address + 1, (byte) ((value >> 8) & 0xFF));
            cycles -= 2;
        }

        public int spToAddress() {
            return 0x100 | SP;
        }

        public void pushWordToStack(int cycles, Mem memory, int value) {
            writeByte((byte) (value >> 8), cycles, spToAddress(), memory);
            SP--;
            writeByte((byte) (value & 0xFF), cycles, spToAddress(), memory);
            SP--;
        }

        public void pushPCMinusOneToStack(int cycles, Mem memory) {
            pushWordToStack(cycles, memory, PC - 1);
        }

        public void pushPCPlusOneToStack(int cycles, Mem memory) {
            pushWordToStack(cycles, memory, PC + 1);
        }

        public void pushPCToStack(int cycles, Mem memory) {
            pushWordToStack(cycles, memory, PC);
        }

        public void pushByteOntoStack(int cycles, byte value, Mem memory) {
            int spWord = spToAddress();
            memory.write(spWord, value);
            cycles--;
            SP--;
            cycles--;
        }

        public byte popByteFromStack(int cycles, Mem memory) {
            SP++;
            cycles--;
            int spWord = spToAddress();
            byte value = memory.read(spWord);
            cycles--;
            return value;
        }

        public int popWordFromStack(int cycles, Mem memory) {
            int valueFromStack = readWord(cycles, spToAddress() + 1, memory);
            SP += 2;
            cycles--;
            return valueFromStack;
        }

        public void ADC() {
            //A,Z,C,N = A+M+C
            LDASetStatus();
        }

        public void AND() {
            //AND - Logical AND A,Z,N = A&M
            LDASetStatus();
        }

        public void ASL() {
            //ASL - Arithmetic Shift Left A,Z,C,N = M*2 or M,Z,C,N = M*2
            LDASetStatus();
        }

        public void BCC() {
            //BCC - Branch if Carry Clear
        }

        public void BCS() {
            //BCS - Branch if Carry Set
        }

        public void BEQ() {
            //BEQ - Branch if Equal
        }

        public void BIT() {
            //BIT - Bit Test 
            //A & M, N = M7, V = M6
            Flag.V = ((A & 0b01000000) > 0) ? 1 : 0;
            Flag.Z = (A == 0) ? 1 : 0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;
        }

        public void BMI() {
            //BMI - Branch if Minus
        }

        public void BNE() {
            //BNE - Branch if Not Equal
        }

        public void BPL() {
            //BPL - Branch if Positive
        }

        public void BRK() {
            //BRK - Force Interrupt
            Flag.B = 1;
        }

        public void BVC() {
            //BVC - Branch if Overflow Clear
        }

        public void BVS() {
            //BVS - Branch if Overflow Set

        }

        public void CLC() {
            Flag.C = 0;
        }

        public void CLD() {
            Flag.D = 0;
        }

        public void CLI() {
            Flag.I = 0;
        }

        public void CLV() {
            Flag.V = 0;
        }

        public void CMP(Mem memory) {
            //CMP - Compare Z,C,N = A-M

            //Flag.C = (A >= M) ? 1 : 0;
            //Flag.Z = (A == M) ? 1 : 0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;
        }

        public void CPX() {
            //CPX - Compare X Register
            //Z,C,N = X - M

            //Flag.C = (X >= M) ? 1 : 0;
            //Flag.Z = (X == M) ? 1 : 0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;
        }

        public void CPY() {
            //CPY - Compare Y Register
            //Z,C,N = Y - M
            //Flag.C = (Y >= M) ? 1 : 0;
            //Flag.Z = (Y = M) ? 1 : 0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;
        }

        public void DEC() {
            //DEC - Decrement Memory M,Z,N = M-1
            //Flag.Z = (RES==0)?1:0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;
        }

        public void DEX() {
            //DEX - Decrement X Register X,Z,N = X-1
            Flag.Z = (X == 0) ? 1 : 0;
            Flag.N = ((X & 0b10000000) > 0) ? 1 : 0;
        }

        public void DEY() {
            //DEY - Decrement Y Register Y,Z,N = Y-1
            Flag.Z = (Y == 0) ? 1 : 0;
            Flag.N = ((Y & 0b10000000) > 0) ? 1 : 0;
        }

        public void EOR() {
            //EOR - Exclusive OR A,Z,N = A^M
            Flag.Z = (A == 0) ? 1 : 0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;

        }

        public void INC() {
            //INC - Increment Memory M,Z,N = M+1
            //Flag.Z = (RES == 0)?1:0;
            //Flag.N = ((RES & 0b10000000) > 0) ? 1 : 0;
        }

        public void INX() {
            //INX - Increment X Register X,Z,N = X+1
            X++;
            Flag.Z = (X == 0) ? 0 : 1;
            Flag.N = ((X & 0b10000000) > 0) ? 1 : 0;
        }

        public void INY() {
            //INY - Increment Y Register Y,Z,N = Y+1
            Y++;
            Flag.Z = (Y == 0) ? 0 : 1;
            Flag.N = ((Y & 0b10000000) > 0) ? 1 : 0;
        }

        public void JMP() {
            //JMP - Jump
        }

        public void JSR(int cycles, Mem memory) {
            //JSR - Jump to Subroutine
            int subAdrr = fetchWord(cycles, memory);
            writeWord(PC - 1, cycles, SP, memory);
            PC = subAdrr;
            cycles--;
        }

        public void LDA(Mem memory) {
            //LDA - Load Accumulator A,Z,N = M
            A = memory.Data[PC]; // Carga el valor inmediato en A
            PC++; // Incrementa PC después de cargar el operando
            LDASetStatus();
        }

        public void LDX() {
            //LDX - Load X Register X,Z,N = M
            Flag.Z = (X == 0) ? 0 : 1;
            Flag.N = ((X & 0b10000000) > 0) ? 1 : 0;
        }

        public void LDY() {
            //LDY - Load Y Register Y,Z,N = M
            Flag.Z = (Y == 0) ? 0 : 1;
            Flag.N = ((Y & 0b10000000) > 0) ? 1 : 0;
        }

        public void LSR() {
            //LSR - Logical Shift Right A,C,Z,N = A/2 or M,C,Z,N = M/2
            //Flag.C = Set to contents of old bit 0
            //Flag.Z = (RES == 0)?1:0;
            //Flag.N = ((RES & 0b10000000) > 0) ? 1 : 0;
        }

        public void NOP() {
            //NOP - No Operation
        }

        public void ORA() {
            //ORA - Logical Inclusive OR A,Z,N = A|M
            LDASetStatus();
        }

        public void PHA() {
            //PHA - Push Accumulator
        }

        public void PHP() {
            //PHP - Push Processor Status
        }

        public void PLA() {
            //PLA - Pull Accumulator
            LDASetStatus();
        }

        public void PLP() {
            //PLP - Pull Processor Status
            //Flag.* - from stack
        }

        public void ROL() {
            //C	Carry Flag	Set to contents of old bit 7
            Flag.Z = (A == 0) ? 1 : 0;

            //Flag.N = ((RES & 0b10000000) > 0) ? 1 : 0;
        }

        public void ROR() {
            //C	Carry Flag	Set to contents of old bit 7
            Flag.Z = (A == 0) ? 1 : 0;

            //Flag.N = ((RES & 0b10000000) > 0) ? 1 : 0;
        }

        public void RTI() {
            //RTI - Return from Interrupt
            //Flag.* = from stack
        }

        public void RTS() {
            //RTS - Return from Subroutine
        }

        public void SBC() {
            //SBC - Subtract with Carry A,Z,C,N = A-M-(1-C)
            //Flag.C = Clear if overflow in bit 7
            Flag.Z = (A == 0) ? 1 : 0;
            //Flag.V= Overflow Flag	Set if sign bit is incorrect
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;
        }

        public void SEC() {
            //SEC - Set Carry Flag
            Flag.C = 1;
        }

        public void SED() {
            //SED - Set Decimal Flag
            Flag.D = 1;
        }

        public void SEI() {
            //SEI - Set Interrupt Disable
            Flag.I = 1;
        }

        public void STA() {
            //STA - Store Accumulator
            //M = A
        }

        public void STX() {
            //STX - Store X Register
            //M = X
        }

        public void STY() {
            //STY - Store Y Register
            //M = Y
        }

        public void TAX() {
            //TAX - Transfer Accumulator to X
            Flag.Z = (X == 0) ? 1 : 0;
            Flag.N = ((X & 0b10000000) > 0) ? 1 : 0;
        }

        public void TAY() {
            //TAY - Transfer Accumulator to Y
            Flag.Z = (Y == 0) ? 1 : 0;
            Flag.N = ((Y & 0b10000000) > 0) ? 1 : 0;
        }

        public void TSX() {
            //TSX - Transfer Stack Pointer to X
            //X = Stack;
            Flag.Z = (X == 0) ? 1 : 0;
            Flag.N = ((X & 0b10000000) > 0) ? 1 : 0;

        }

        public void TXA() {
            //TXA - Transfer X to Accumulator
            //A = Stack;
            Flag.Z = (A == 0) ? 1 : 0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;

        }

        public void TXS() {
            //TXS - Transfer X to Stack Pointer
            //Stack = X;

        }

        public void TYA() {
            //TYA - Transfer Y to Accumulator
            A = Y;
            LDASetStatus();
        }

        public void LDASetStatus() {
            Flag.Z = (A == 0) ? 1 : 0;
            Flag.N = ((A & 0b10000000) > 0) ? 1 : 0;
        }

        public void execute(int cycles, Mem memory) {
            while (cycles > 0) {
                byte Inst = fetchByte(cycles, memory);
                switch (Inst) {
                    case Opcode.LDA:
                        System.out.println("LDA - C: " + cycles);
                        byte lda = fetchByte(2, memory);
                        A = lda;
                        LDASetStatus();
                        break;
                    case Opcode.absolute:
                        System.out.println("ABS - C: " + cycles);
                        byte abs = fetchByte(4, memory);
                        A = readByte(4, abs, memory);
                        LDASetStatus();
                        break;
                    case Opcode.absolute_x:
                        System.out.println("ABS_X - C: " + cycles);
                        byte absx = fetchByte(4, memory);
                        A = readByte(4, absx, memory);
                        LDASetStatus();
                        break;
                    case Opcode.absolute_y:
                        System.out.println("ABS_Y - C: " + cycles);
                        byte absy = fetchByte(4, memory);
                        A = readByte(4, absy, memory);
                        LDASetStatus();
                        break;
                    case Opcode.zero_page:
                        System.out.println("ZERO PAGE - C: " + cycles);
                        byte zpa = fetchByte(3, memory);
                        A = readByte(3, zpa, memory);
                        LDASetStatus();
                        break;
                    case Opcode.zero_page_x:
                        System.out.println("ZERO PAGE X - C: " + cycles);
                        byte zpax = fetchByte(cycles, memory); // 4 cycles
                        zpax += X;
                        cycles--;
                        A = readByte(cycles, zpax, memory);
                        LDASetStatus();
                        break;
                    case Opcode.INX:
                        System.out.println("INDIRECT X - C: " + cycles);
                        byte indx = fetchByte(6, memory);
                        A = readByte(6, indx, memory);
                        LDASetStatus();
                        break;
                    case Opcode.INY:
                        System.out.println("INDIRECT Y - C: " + cycles);
                        byte indy = fetchByte(5, memory);
                        A = readByte(5, indy, memory);
                        LDASetStatus();
                        break;
                    case Opcode.BRK:
                        System.out.println("BRK");
                        BRK();
                        break;
                    default:
                        System.out.println("Instruction not handled: " + (char) Inst);
                        break;
                }

            }
        }

        // Métodos de dirección (addressing modes) aquí...
        // Métodos de operaciones (opcodes) aquí...
        // Métodos para manejar flags aquí...
        // Otros métodos de control y utilidad aquí...
        String showCPUInfo() {
            String info = "xCPU{" + "PC=" + PC + ", SP=" + SP + ", A=" + A + ", X=" + X + ", Y=" + Y + ", Flag=" + Flag.getStatus() + '}';;
            return info;
        }
    }

    // Constantes de opcodes aquí...
    // Métodos para cargar programas, imprimir estado, y ejecutar ciclo de instrucción aquí...
    // Main o punto de entrada para probar la emulación aquí...
}
