/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;

/**
 * This class acts as a facade to the internal Scanner implementation and delegates
 * all the work to the internal Scanner instance.
 *
 * <p>
 * <b>Note for maintainers</b>
 * No additional logic should be added here except adopting NON-API constants
 * returned by {@link #getNextToken()} from {@link TerminalTokens} to {@link ITerminalSymbols}.
 */
public class PublicScanner implements IScanner, ITerminalSymbols {

	private final Scanner delegate;

	public PublicScanner(
			boolean tokenizeComments,
			boolean tokenizeWhiteSpace,
			boolean checkNonExternalizedStringLiterals,
			long sourceLevel,
			long complianceLevel,
			char[][] taskTags,
			char[][] taskPriorities,
			boolean isTaskCaseSensitive,
			boolean isPreviewEnabled,
			boolean recordLineSeparator) {

			this.delegate = createScanner(tokenizeComments, tokenizeWhiteSpace, checkNonExternalizedStringLiterals, sourceLevel,
					complianceLevel, taskTags, taskPriorities, isTaskCaseSensitive, isPreviewEnabled);
			this.delegate.recordLineSeparator = recordLineSeparator;
	}

	protected Scanner createScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace,
			boolean checkNonExternalizedStringLiterals, long sourceLevel, long complianceLevel, char[][] taskTags,
			char[][] taskPriorities, boolean isTaskCaseSensitive, boolean isPreviewEnabled) {
		return new Scanner(tokenizeComments, tokenizeWhiteSpace, checkNonExternalizedStringLiterals,
				sourceLevel, complianceLevel, taskTags, taskPriorities, isTaskCaseSensitive, isPreviewEnabled);
	}

	@Override
	public int getCurrentTokenEndPosition(){
		return this.delegate.getCurrentTokenEndPosition();
	}

	@Override
	public char[] getCurrentTokenSource() {
		return this.delegate.getCurrentTokenSource();
	}

	@Override
	public final char[] getRawTokenSource() {
		return this.delegate.getRawTokenSource();
	}

	@Override
	public int getCurrentTokenStartPosition(){
		return this.delegate.startPosition;
	}

	/*
	 * Search the source position corresponding to the end of a given line number
	 *
	 * Line numbers are 1-based, and relative to the scanner initialPosition.
	 * Character positions are 0-based.
	 *
	 * In case the given line number is inconsistent, answers -1.
	 */
	@Override
	public final int getLineEnd(int lineNumber) {
		return this.delegate.getLineEnd(lineNumber);
	}

	@Override
	public final int[] getLineEnds() {
		return this.delegate.getLineEnds();
	}

	/**
	 * Search the source position corresponding to the beginning of a given line number
	 *
	 * Line numbers are 1-based, and relative to the scanner initialPosition.
	 * Character positions are 0-based.
	 *
	 * e.g.	getLineStart(1) --> 0	indicates that the first line starts at character 0.
	 *
	 * In case the given line number is inconsistent, answers -1.
	 *
	 * @param lineNumber int
	 * @return int
	 */
	@Override
	public final int getLineStart(int lineNumber) {
		return this.delegate.getLineStart(lineNumber);
	}

	@Override
	public int getNextToken() throws InvalidInputException {
		int nextToken = this.delegate.getNextToken();
		int symbol  = translateTokenToTerminalSymbol(nextToken);
		return symbol;
	}

	/**
	 * Translates internal generated constants from {@link TerminalTokens} to public constants defined in
	 * {@link ITerminalSymbols}.
	 * <p>
	 * There is PublicScannerTest that validates the translation and can generate switch body for this method
	 * on changes in Scanner / TerminalTokens.
	 * <p>
	 * Note for maintainers: new tokens from {@link TerminalTokens} should be added to {@link ITerminalSymbols} with
	 * <b>adopted</b> numerical values (they differ in each interface).
	 */
	@Deprecated // uses deprecated TerminalTokensIdentifier
	private int translateTokenToTerminalSymbol(int nextToken) throws InvalidInputException {
		switch (nextToken) {
			case TerminalTokens.TokenNameAND : nextToken = ITerminalSymbols.TokenNameAND; break;
			case TerminalTokens.TokenNameAND_AND : nextToken = ITerminalSymbols.TokenNameAND_AND; break;
			case TerminalTokens.TokenNameAND_EQUAL : nextToken = ITerminalSymbols.TokenNameAND_EQUAL; break;
			case TerminalTokens.TokenNameARROW : nextToken = ITerminalSymbols.TokenNameARROW; break;
			case TerminalTokens.TokenNameAT : nextToken = ITerminalSymbols.TokenNameAT; break;
			case TerminalTokens.TokenNameAT308 : nextToken = ITerminalSymbols.TokenNameAT; break;
			case TerminalTokens.TokenNameAT308DOTDOTDOT : nextToken = ITerminalSymbols.TokenNameAT; break;
			case TerminalTokens.TokenNameBeginCasePattern : nextToken = getNextToken(); break;
			case TerminalTokens.TokenNameCaseArrow : nextToken = ITerminalSymbols.TokenNameARROW; break;
			case TerminalTokens.TokenNameBeginIntersectionCast : nextToken = getNextToken(); break;
			case TerminalTokens.TokenNameBeginLambda : nextToken = getNextToken(); break;
			case TerminalTokens.TokenNameBeginTypeArguments : nextToken = getNextToken(); break;
			case TerminalTokens.TokenNameCOLON : nextToken = ITerminalSymbols.TokenNameCOLON; break;
			case TerminalTokens.TokenNameCOLON_COLON : nextToken = ITerminalSymbols.TokenNameCOLON_COLON; break;
			case TerminalTokens.TokenNameCOMMA : nextToken = ITerminalSymbols.TokenNameCOMMA; break;
			case TerminalTokens.TokenNameCOMMENT_BLOCK : nextToken = ITerminalSymbols.TokenNameCOMMENT_BLOCK; break;
			case TerminalTokens.TokenNameCOMMENT_JAVADOC : nextToken = ITerminalSymbols.TokenNameCOMMENT_JAVADOC; break;
			case TerminalTokens.TokenNameCOMMENT_MARKDOWN : nextToken = ITerminalSymbols.TokenNameCOMMENT_MARKDOWN; break;
			case TerminalTokens.TokenNameCOMMENT_LINE : nextToken = ITerminalSymbols.TokenNameCOMMENT_LINE; break;
			case TerminalTokens.TokenNameCharacterLiteral : nextToken = ITerminalSymbols.TokenNameCharacterLiteral; break;
			case TerminalTokens.TokenNameDIVIDE : nextToken = ITerminalSymbols.TokenNameDIVIDE; break;
			case TerminalTokens.TokenNameDIVIDE_EQUAL : nextToken = ITerminalSymbols.TokenNameDIVIDE_EQUAL; break;
			case TerminalTokens.TokenNameDOT : nextToken = ITerminalSymbols.TokenNameDOT; break;
			case TerminalTokens.TokenNameDoubleLiteral : nextToken = ITerminalSymbols.TokenNameDoubleLiteral; break;
			case TerminalTokens.TokenNameELLIPSIS : nextToken = ITerminalSymbols.TokenNameELLIPSIS; break;
			case TerminalTokens.TokenNameEOF : nextToken = ITerminalSymbols.TokenNameEOF; break;
			case TerminalTokens.TokenNameEQUAL : nextToken = ITerminalSymbols.TokenNameEQUAL; break;
			case TerminalTokens.TokenNameEQUAL_EQUAL : nextToken = ITerminalSymbols.TokenNameEQUAL_EQUAL; break;
			case TerminalTokens.TokenNameERROR : nextToken = ITerminalSymbols.TokenNameERROR; break;
			case TerminalTokens.TokenNameElidedSemicolonAndRightBrace : nextToken = getNextToken(); break;
			case TerminalTokens.TokenNameFloatingPointLiteral : nextToken = ITerminalSymbols.TokenNameFloatingPointLiteral; break;
			case TerminalTokens.TokenNameGREATER : nextToken = ITerminalSymbols.TokenNameGREATER; break;
			case TerminalTokens.TokenNameGREATER_EQUAL : nextToken = ITerminalSymbols.TokenNameGREATER_EQUAL; break;
			case TerminalTokens.TokenNameIdentifier : nextToken = ITerminalSymbols.TokenNameIdentifier; break;
			case TerminalTokens.TokenNameIntegerLiteral : nextToken = ITerminalSymbols.TokenNameIntegerLiteral; break;
			case TerminalTokens.TokenNameLBRACE : nextToken = ITerminalSymbols.TokenNameLBRACE; break;
			case TerminalTokens.TokenNameLBRACKET : nextToken = ITerminalSymbols.TokenNameLBRACKET; break;
			case TerminalTokens.TokenNameLEFT_SHIFT : nextToken = ITerminalSymbols.TokenNameLEFT_SHIFT; break;
			case TerminalTokens.TokenNameLEFT_SHIFT_EQUAL : nextToken = ITerminalSymbols.TokenNameLEFT_SHIFT_EQUAL; break;
			case TerminalTokens.TokenNameLESS : nextToken = ITerminalSymbols.TokenNameLESS; break;
			case TerminalTokens.TokenNameLESS_EQUAL : nextToken = ITerminalSymbols.TokenNameLESS_EQUAL; break;
			case TerminalTokens.TokenNameLPAREN : nextToken = ITerminalSymbols.TokenNameLPAREN; break;
			case TerminalTokens.TokenNameLongLiteral : nextToken = ITerminalSymbols.TokenNameLongLiteral; break;
			case TerminalTokens.TokenNameMINUS : nextToken = ITerminalSymbols.TokenNameMINUS; break;
			case TerminalTokens.TokenNameMINUS_EQUAL : nextToken = ITerminalSymbols.TokenNameMINUS_EQUAL; break;
			case TerminalTokens.TokenNameMINUS_MINUS : nextToken = ITerminalSymbols.TokenNameMINUS_MINUS; break;
			case TerminalTokens.TokenNameMULTIPLY : nextToken = ITerminalSymbols.TokenNameMULTIPLY; break;
			case TerminalTokens.TokenNameMULTIPLY_EQUAL : nextToken = ITerminalSymbols.TokenNameMULTIPLY_EQUAL; break;
			case TerminalTokens.TokenNameNOT : nextToken = ITerminalSymbols.TokenNameNOT; break;
			case TerminalTokens.TokenNameNOT_EQUAL : nextToken = ITerminalSymbols.TokenNameNOT_EQUAL; break;
			case TerminalTokens.TokenNameNotAToken : nextToken = ITerminalSymbols.TokenNameNotAToken; break;
			case TerminalTokens.TokenNameOR : nextToken = ITerminalSymbols.TokenNameOR; break;
			case TerminalTokens.TokenNameOR_EQUAL : nextToken = ITerminalSymbols.TokenNameOR_EQUAL; break;
			case TerminalTokens.TokenNameOR_OR : nextToken = ITerminalSymbols.TokenNameOR_OR; break;
			case TerminalTokens.TokenNamePLUS : nextToken = ITerminalSymbols.TokenNamePLUS; break;
			case TerminalTokens.TokenNamePLUS_EQUAL : nextToken = ITerminalSymbols.TokenNamePLUS_EQUAL; break;
			case TerminalTokens.TokenNamePLUS_PLUS : nextToken = ITerminalSymbols.TokenNamePLUS_PLUS; break;
			case TerminalTokens.TokenNameQUESTION : nextToken = ITerminalSymbols.TokenNameQUESTION; break;
			case TerminalTokens.TokenNameRBRACE : nextToken = ITerminalSymbols.TokenNameRBRACE; break;
			case TerminalTokens.TokenNameRBRACKET : nextToken = ITerminalSymbols.TokenNameRBRACKET; break;
			case TerminalTokens.TokenNameREMAINDER : nextToken = ITerminalSymbols.TokenNameREMAINDER; break;
			case TerminalTokens.TokenNameREMAINDER_EQUAL : nextToken = ITerminalSymbols.TokenNameREMAINDER_EQUAL; break;
			case TerminalTokens.TokenNameRIGHT_SHIFT : nextToken = ITerminalSymbols.TokenNameRIGHT_SHIFT; break;
			case TerminalTokens.TokenNameRIGHT_SHIFT_EQUAL : nextToken = ITerminalSymbols.TokenNameRIGHT_SHIFT_EQUAL; break;
			case TerminalTokens.TokenNameRPAREN : nextToken = ITerminalSymbols.TokenNameRPAREN; break;
			case TerminalTokens.TokenNameRestrictedIdentifierYield : nextToken = ITerminalSymbols.TokenNameRestrictedIdentifierYield; break;
			case TerminalTokens.TokenNameRestrictedIdentifierpermits : nextToken = ITerminalSymbols.TokenNameRestrictedIdentifierpermits; break;
			case TerminalTokens.TokenNameRestrictedIdentifierrecord : nextToken = ITerminalSymbols.TokenNameRestrictedIdentifierrecord; break;
			case TerminalTokens.TokenNameRestrictedIdentifiersealed : nextToken = ITerminalSymbols.TokenNameRestrictedIdentifiersealed; break;
			case TerminalTokens.TokenNameSEMICOLON : nextToken = ITerminalSymbols.TokenNameSEMICOLON; break;
			case TerminalTokens.TokenNameSingleQuoteStringLiteral : nextToken = ITerminalSymbols.TokenNameSingleQuoteStringLiteral; break;
			case TerminalTokens.TokenNameStringLiteral : nextToken = ITerminalSymbols.TokenNameStringLiteral; break;
			case TerminalTokens.TokenNameTWIDDLE : nextToken = ITerminalSymbols.TokenNameTWIDDLE; break;
			case TerminalTokens.TokenNameTextBlock : nextToken = ITerminalSymbols.TokenNameTextBlock; break;
			case TerminalTokens.TokenNameUNSIGNED_RIGHT_SHIFT : nextToken = ITerminalSymbols.TokenNameUNSIGNED_RIGHT_SHIFT; break;
			case TerminalTokens.TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL : nextToken = ITerminalSymbols.TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL; break;
			case TerminalTokens.TokenNameWHITESPACE : nextToken = ITerminalSymbols.TokenNameWHITESPACE; break;
			case TerminalTokens.TokenNameXOR : nextToken = ITerminalSymbols.TokenNameXOR; break;
			case TerminalTokens.TokenNameXOR_EQUAL : nextToken = ITerminalSymbols.TokenNameXOR_EQUAL; break;
			case TerminalTokens.TokenNameAbstract : nextToken = ITerminalSymbols.TokenNameabstract; break;
			case TerminalTokens.TokenNameAssert : nextToken = ITerminalSymbols.TokenNameassert; break;
			case TerminalTokens.TokenNameBoolean : nextToken = ITerminalSymbols.TokenNameboolean; break;
			case TerminalTokens.TokenNameBreak : nextToken = ITerminalSymbols.TokenNamebreak; break;
			case TerminalTokens.TokenNameByte : nextToken = ITerminalSymbols.TokenNamebyte; break;
			case TerminalTokens.TokenNameCase : nextToken = ITerminalSymbols.TokenNamecase; break;
			case TerminalTokens.TokenNameCatch : nextToken = ITerminalSymbols.TokenNamecatch; break;
			case TerminalTokens.TokenNameChar : nextToken = ITerminalSymbols.TokenNamechar; break;
			case TerminalTokens.TokenNameClass : nextToken = ITerminalSymbols.TokenNameclass; break;
			case TerminalTokens.TokenNameConst : nextToken = ITerminalSymbols.TokenNameconst; break;
			case TerminalTokens.TokenNameContinue : nextToken = ITerminalSymbols.TokenNamecontinue; break;
			case TerminalTokens.TokenNameDefault : nextToken = ITerminalSymbols.TokenNamedefault; break;
			case TerminalTokens.TokenNameDo : nextToken = ITerminalSymbols.TokenNamedo; break;
			case TerminalTokens.TokenNameDouble : nextToken = ITerminalSymbols.TokenNamedouble; break;
			case TerminalTokens.TokenNameElse : nextToken = ITerminalSymbols.TokenNameelse; break;
			case TerminalTokens.TokenNameEnum : nextToken = ITerminalSymbols.TokenNameenum; break;
			case TerminalTokens.TokenNameExtends : nextToken = ITerminalSymbols.TokenNameextends; break;
			case TerminalTokens.TokenNameFalse : nextToken = ITerminalSymbols.TokenNamefalse; break;
			case TerminalTokens.TokenNameFinal : nextToken = ITerminalSymbols.TokenNamefinal; break;
			case TerminalTokens.TokenNameFinally : nextToken = ITerminalSymbols.TokenNamefinally; break;
			case TerminalTokens.TokenNameFloat : nextToken = ITerminalSymbols.TokenNamefloat; break;
			case TerminalTokens.TokenNameFor : nextToken = ITerminalSymbols.TokenNamefor; break;
			case TerminalTokens.TokenNameGoto : nextToken = ITerminalSymbols.TokenNamegoto; break;
			case TerminalTokens.TokenNameIf : nextToken = ITerminalSymbols.TokenNameif; break;
			case TerminalTokens.TokenNameImplements : nextToken = ITerminalSymbols.TokenNameimplements; break;
			case TerminalTokens.TokenNameImport : nextToken = ITerminalSymbols.TokenNameimport; break;
			case TerminalTokens.TokenNameInstanceof : nextToken = ITerminalSymbols.TokenNameinstanceof; break;
			case TerminalTokens.TokenNameInt : nextToken = ITerminalSymbols.TokenNameint; break;
			case TerminalTokens.TokenNameInterface : nextToken = ITerminalSymbols.TokenNameinterface; break;
			case TerminalTokens.TokenNameLong : nextToken = ITerminalSymbols.TokenNamelong; break;
			case TerminalTokens.TokenNameNative : nextToken = ITerminalSymbols.TokenNamenative; break;
			case TerminalTokens.TokenNameNew : nextToken = ITerminalSymbols.TokenNamenew; break;
			case TerminalTokens.TokenNameNonSealed : nextToken = ITerminalSymbols.TokenNamenon_sealed; break;
			case TerminalTokens.TokenNameNull : nextToken = ITerminalSymbols.TokenNamenull; break;
			case TerminalTokens.TokenNamePackage : nextToken = ITerminalSymbols.TokenNamepackage; break;
			case TerminalTokens.TokenNamePrivate : nextToken = ITerminalSymbols.TokenNameprivate; break;
			case TerminalTokens.TokenNameProtected : nextToken = ITerminalSymbols.TokenNameprotected; break;
			case TerminalTokens.TokenNamePublic : nextToken = ITerminalSymbols.TokenNamepublic; break;
			case TerminalTokens.TokenNameReturn : nextToken = ITerminalSymbols.TokenNamereturn; break;
			case TerminalTokens.TokenNameShort : nextToken = ITerminalSymbols.TokenNameshort; break;
			case TerminalTokens.TokenNameStatic : nextToken = ITerminalSymbols.TokenNamestatic; break;
			case TerminalTokens.TokenNameStrictfp : nextToken = ITerminalSymbols.TokenNamestrictfp; break;
			case TerminalTokens.TokenNameSuper : nextToken = ITerminalSymbols.TokenNamesuper; break;
			case TerminalTokens.TokenNameSwitch : nextToken = ITerminalSymbols.TokenNameswitch; break;
			case TerminalTokens.TokenNameSynchronized : nextToken = ITerminalSymbols.TokenNamesynchronized; break;
			case TerminalTokens.TokenNameThis : nextToken = ITerminalSymbols.TokenNamethis; break;
			case TerminalTokens.TokenNameThrow : nextToken = ITerminalSymbols.TokenNamethrow; break;
			case TerminalTokens.TokenNameThrows : nextToken = ITerminalSymbols.TokenNamethrows; break;
			case TerminalTokens.TokenNameTransient : nextToken = ITerminalSymbols.TokenNametransient; break;
			case TerminalTokens.TokenNameTrue : nextToken = ITerminalSymbols.TokenNametrue; break;
			case TerminalTokens.TokenNameTry : nextToken = ITerminalSymbols.TokenNametry; break;
			case TerminalTokens.TokenNameVoid : nextToken = ITerminalSymbols.TokenNamevoid; break;
			case TerminalTokens.TokenNameVolatile : nextToken = ITerminalSymbols.TokenNamevolatile; break;
			case TerminalTokens.TokenNameWhile : nextToken = ITerminalSymbols.TokenNamewhile; break;
			case TerminalTokens.TokenNameRestrictedIdentifierWhen : nextToken = ITerminalSymbols.TokenNameRestrictedIdentifierWhen; break;
			case TerminalTokens.TokenNameUNDERSCORE : nextToken = ITerminalSymbols.TokenNameUNDERSCORE; break;
			default:
				throw Scanner.invalidToken(nextToken);
		}
		return nextToken;
	}

	@Override
	public char[] getSource(){
		return this.delegate.getSource();
	}

	/**
	 * Reposition the scanner on some portion of the original source. The given endPosition is the last valid position.
	 * Beyond this position, the scanner will answer EOF tokens (<code>ITerminalSymbols.TokenNameEOF</code>).
	 *
	 * @param begin the given start position
	 * @param end the given end position
	 */
	@Override
	public void resetTo(int begin, int end) {
		this.delegate.resetTo(begin, end);
	}

	/**
	 * Search the line number corresponding to a specific position
	 * @param position int
	 * @return int
	 */
	@Override
	public final int getLineNumber(int position) {
		return this.delegate.getLineNumber(position);
	}

	@Override
	public final void setSource(char[] sourceString){
		this.delegate.setSource(sourceString);
	}

	@Override
	public String toString() {
		return this.delegate.toString();
	}

}
