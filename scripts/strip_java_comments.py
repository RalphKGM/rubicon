#!/usr/bin/env python3
"""
Strip Java comments (// and /* */) from all .java files under the workspace
except files literally named Main.java. Preserves string and char literals.
"""
import sys
import os

def strip_comments_java(src: str) -> str:
    out = []
    i = 0
    n = len(src)
    state = 'normal'  # normal, sl_comment, ml_comment, string, char
    while i < n:
        ch = src[i]
        nxt = src[i+1] if i+1 < n else ''
        if state == 'normal':
            if ch == '/' and nxt == '/':
                state = 'sl_comment'
                i += 2
                continue
            elif ch == '/' and nxt == '*':
                state = 'ml_comment'
                i += 2
                continue
            elif ch == '"':
                out.append(ch)
                state = 'string'
                i += 1
                continue
            elif ch == "'":
                out.append(ch)
                state = 'char'
                i += 1
                continue
            else:
                out.append(ch)
                i += 1
                continue
        elif state == 'sl_comment':
            # skip until end of line
            if ch == '\n':
                out.append(ch)
                state = 'normal'
            i += 1
            continue
        elif state == 'ml_comment':
            # skip until */
            if ch == '*' and nxt == '/':
                state = 'normal'
                i += 2
            else:
                i += 1
            continue
        elif state == 'string':
            out.append(ch)
            if ch == '\\':
                # escape, include next char also
                if i+1 < n:
                    out.append(src[i+1])
                    i += 2
                else:
                    i += 1
            elif ch == '"':
                state = 'normal'
                i += 1
            else:
                i += 1
            continue
        elif state == 'char':
            out.append(ch)
            if ch == '\\':
                if i+1 < n:
                    out.append(src[i+1])
                    i += 2
                else:
                    i += 1
            elif ch == "'":
                state = 'normal'
                i += 1
            else:
                i += 1
            continue
    return ''.join(out)

def main(root):
    modified = []
    for dirpath, dirnames, filenames in os.walk(root):
        # skip target directories to avoid editing generated classes
        if 'target' in dirpath.split(os.sep):
            continue
        for fn in filenames:
            if not fn.endswith('.java'):
                continue
            if fn == 'Main.java':
                continue
            path = os.path.join(dirpath, fn)
            try:
                with open(path, 'r', encoding='utf-8') as f:
                    src = f.read()
            except Exception as e:
                print(f"Skipping {path}: {e}")
                continue
            new = strip_comments_java(src)
            if new != src:
                with open(path, 'w', encoding='utf-8') as f:
                    f.write(new)
                modified.append(path)
    print(f"Stripped comments from {len(modified)} files")
    for p in modified:
        print(p)

if __name__ == '__main__':
    root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
    if len(sys.argv) > 1:
        root = sys.argv[1]
    main(root)
